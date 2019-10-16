package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.redis.connection.RedisConnection;
import cn.choleece.base.framework.redis.connection.RedisConnectionFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.ResourceHolder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author choleece
 * @Description: Redis 连接工具
 * @Date 2019-10-15 22:07
 **/
public abstract class RedisConnectionUtils {

    private static final Log log = LogFactory.getLog(RedisConnectionUtils.class);

    public RedisConnectionUtils() {
    }

    public static RedisConnection bindConnection(RedisConnectionFactory factory) {
        return bindConnection(factory, false);
    }

    public static RedisConnection bindConnection(RedisConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, true, enableTranactionSupport);
    }

    public static RedisConnection getConnection(RedisConnectionFactory factory) {
        return getConnection(factory, false);
    }

    public static RedisConnection getConnection(RedisConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, false, enableTranactionSupport);
    }

    public static RedisConnection doGetConnection(RedisConnectionFactory factory, boolean allowCreate, boolean bind, boolean enableTransactionSupport) {
        Assert.notNull(factory, "No RedisConnectionFactory specified");
        RedisConnectionUtils.RedisConnectionHolder connHolder = (RedisConnectionUtils.RedisConnectionHolder)TransactionSynchronizationManager.getResource(factory);
        if (connHolder != null) {
            if (enableTransactionSupport) {
                potentiallyRegisterTransactionSynchronisation(connHolder, factory);
            }

            return connHolder.getConnection();
        } else if (!allowCreate) {
            throw new IllegalArgumentException("No connection found and allowCreate = false");
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Opening RedisConnection");
            }

            RedisConnection conn = factory.getConnection();
            if (bind) {
                RedisConnection connectionToBind = conn;
                if (enableTransactionSupport && isActualNonReadonlyTransactionActive()) {
                    connectionToBind = createConnectionProxy(conn, factory);
                }

                connHolder = new RedisConnectionUtils.RedisConnectionHolder(connectionToBind);
                TransactionSynchronizationManager.bindResource(factory, connHolder);
                if (enableTransactionSupport) {
                    potentiallyRegisterTransactionSynchronisation(connHolder, factory);
                }

                return connHolder.getConnection();
            } else {
                return conn;
            }
        }
    }

    private static void potentiallyRegisterTransactionSynchronisation(RedisConnectionUtils.RedisConnectionHolder connHolder, RedisConnectionFactory factory) {
        if (isActualNonReadonlyTransactionActive() && !connHolder.isTransactionSyncronisationActive()) {
            connHolder.setTransactionSyncronisationActive(true);
            RedisConnection conn = connHolder.getConnection();
            conn.multi();
            TransactionSynchronizationManager.registerSynchronization(new RedisConnectionUtils.RedisTransactionSynchronizer(connHolder, conn, factory));
        }

    }

    private static boolean isActualNonReadonlyTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive() && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    private static RedisConnection createConnectionProxy(RedisConnection connection, RedisConnectionFactory factory) {
        ProxyFactory proxyFactory = new ProxyFactory(connection);
        proxyFactory.addAdvice(new RedisConnectionUtils.ConnectionSplittingInterceptor(factory));
        return (RedisConnection)RedisConnection.class.cast(proxyFactory.getProxy());
    }

    /** @deprecated */
    @Deprecated
    public static void releaseConnection(@Nullable RedisConnection conn, RedisConnectionFactory factory) {
        releaseConnection(conn, factory, false);
    }

    public static void releaseConnection(@Nullable RedisConnection conn, RedisConnectionFactory factory, boolean transactionSupport) {
        if (conn != null) {
            RedisConnectionUtils.RedisConnectionHolder connHolder = (RedisConnectionUtils.RedisConnectionHolder)TransactionSynchronizationManager.getResource(factory);
            if (connHolder != null && connHolder.isTransactionSyncronisationActive()) {
                if (log.isDebugEnabled()) {
                    log.debug("Redis Connection will be closed when transaction finished.");
                }

            } else {
                if (isConnectionTransactional(conn, factory)) {
                    if (transactionSupport && TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                        if (log.isDebugEnabled()) {
                            log.debug("Unbinding Redis Connection.");
                        }

                        unbindConnection(factory);
                    } else if (log.isDebugEnabled()) {
                        log.debug("Leaving bound Redis Connection attached.");
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Closing Redis Connection.");
                    }

                    conn.close();
                }

            }
        }
    }

    public static void unbindConnection(RedisConnectionFactory factory) {
        RedisConnectionUtils.RedisConnectionHolder connHolder = (RedisConnectionUtils.RedisConnectionHolder)TransactionSynchronizationManager.unbindResourceIfPossible(factory);
        if (connHolder != null) {
            if (connHolder.isTransactionSyncronisationActive()) {
                if (log.isDebugEnabled()) {
                    log.debug("Redis Connection will be closed when outer transaction finished.");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Closing bound connection.");
                }

                RedisConnection connection = connHolder.getConnection();
                connection.close();
            }

        }
    }

    public static boolean isConnectionTransactional(RedisConnection conn, RedisConnectionFactory connFactory) {
        Assert.notNull(connFactory, "No RedisConnectionFactory specified");
        RedisConnectionUtils.RedisConnectionHolder connHolder = (RedisConnectionUtils.RedisConnectionHolder)TransactionSynchronizationManager.getResource(connFactory);
        return connHolder != null && conn == connHolder.getConnection();
    }

    private static class RedisConnectionHolder implements ResourceHolder {
        private boolean unbound;
        private final RedisConnection conn;
        private boolean transactionSyncronisationActive;

        public RedisConnectionHolder(RedisConnection conn) {
            this.conn = conn;
        }

        @Override
        public boolean isVoid() {
            return this.unbound;
        }

        public RedisConnection getConnection() {
            return this.conn;
        }

        @Override
        public void reset() {
        }

        @Override
        public void unbound() {
            this.unbound = true;
        }

        public boolean isTransactionSyncronisationActive() {
            return this.transactionSyncronisationActive;
        }

        public void setTransactionSyncronisationActive(boolean transactionSyncronisationActive) {
            this.transactionSyncronisationActive = transactionSyncronisationActive;
        }
    }

    static class ConnectionSplittingInterceptor implements MethodInterceptor, org.springframework.cglib.proxy.MethodInterceptor {
        private final RedisConnectionFactory factory;

        public ConnectionSplittingInterceptor(RedisConnectionFactory factory) {
            this.factory = factory;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            RedisCommand commandToExecute = RedisCommand.failsafeCommandLookup(method.getName());
            if (this.isPotentiallyThreadBoundCommand(commandToExecute)) {
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug(String.format("Invoke '%s' on bound conneciton", method.getName()));
                }

                return this.invoke(method, obj, args);
            } else {
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug(String.format("Invoke '%s' on unbound conneciton", method.getName()));
                }

                RedisConnection connection = this.factory.getConnection();

                Object var7;
                try {
                    var7 = this.invoke(method, connection, args);
                } finally {
                    if (!connection.isClosed()) {
                        connection.close();
                    }

                }

                return var7;
            }
        }

        private Object invoke(Method method, Object target, Object[] args) throws Throwable {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException var5) {
                throw var5.getCause();
            }
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return this.intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), (MethodProxy)null);
        }

        private boolean isPotentiallyThreadBoundCommand(RedisCommand command) {
            return RedisCommand.UNKNOWN.equals(command) || !command.isReadonly();
        }
    }

    private static class RedisTransactionSynchronizer extends TransactionSynchronizationAdapter {
        private final RedisConnectionUtils.RedisConnectionHolder connHolder;
        private final RedisConnection connection;
        private final RedisConnectionFactory factory;

        @Override
        public void afterCompletion(int status) {
            try {
                switch(status) {
                    case 0:
                        this.connection.exec();
                        break;
                    case 1:
                    case 2:
                    default:
                        this.connection.discard();
                }
            } finally {
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug("Closing bound connection after transaction completed with " + status);
                }

                this.connHolder.setTransactionSyncronisationActive(false);
                this.connection.close();
                TransactionSynchronizationManager.unbindResource(this.factory);
            }

        }

        public RedisTransactionSynchronizer(RedisConnectionUtils.RedisConnectionHolder connHolder, RedisConnection connection, RedisConnectionFactory factory) {
            this.connHolder = connHolder;
            this.connection = connection;
            this.factory = factory;
        }
    }
}
