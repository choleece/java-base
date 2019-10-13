package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author choleece
 * @Description: Redis 节点
 * @Date 2019-10-13 22:27
 **/
public class RedisNode implements NamedNode {

    @Nullable
    String id;

    @Nullable
    String name;

    @Nullable
    String host;

    @Nullable
    int port;

    /**
     * redis 节点类型 （主节点、从节点）
     */
    @Nullable
    RedisNode.NodeType type;

    @Nullable
    String masterId;

    public RedisNode(@Nullable String host, int port) {
        Assert.notNull(host, "host must not be null");

        this.host = host;
        this.port = port;
    }

    protected RedisNode() {
    }

    @Nullable
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    /**
     * 格式化服务器地址
     * @return
     */
    public String asString() {
        return this.host + ":" + this.port;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Override
    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getMasterId() {
        return masterId;
    }

    @Nullable
    public RedisNode.NodeType getType() {
        return type;
    }

    /**
     * 是否为主节点
     * @return
     */
    public boolean isMaster() {
        return ObjectUtils.nullSafeEquals(NodeType.MASTER, this.getType());
    }

    public boolean isSlave() {
        return this.isReplica();
    }

    public static RedisNode.RedisNodeBuilder newRedisNode() {
        return new RedisNode.RedisNodeBuilder();
    }

    public boolean isReplica() {
        return ObjectUtils.nullSafeEquals(NodeType.SLAVE, this.getType());
    }

    @Override
    public String toString() {
        return this.asString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.host);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.port);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && obj instanceof RedisNode) {
            RedisNode other = (RedisNode)obj;
            if (!ObjectUtils.nullSafeEquals(this.host, other.host)) {
                return false;
            } else if (!ObjectUtils.nullSafeEquals(this.port, other.port)) {
                return false;
            } else {
                return ObjectUtils.nullSafeEquals(this.name, other.name);
            }
        } else {
            return false;
        }
    }

    public static class RedisNodeBuilder {

        private RedisNode node = new RedisNode();

        public RedisNodeBuilder() {
        }

        public RedisNode.RedisNodeBuilder withName(String name) {
            this.node.name = name;
            return this;
        }

        public RedisNode.RedisNodeBuilder listeningAt(String host, int port) {
            Assert.hasText(host, "Hostname must not be empty or null.");
            this.node.host = host;
            this.node.port = port;
            return this;
        }

        public RedisNode.RedisNodeBuilder withId(String id) {
            this.node.id = id;
            return this;
        }

        public RedisNode.RedisNodeBuilder promotedAs(RedisNode.NodeType type) {
            this.node.type = type;
            return this;
        }

        public RedisNode.RedisNodeBuilder slaveOf(String masterId) {
            return this.replicaOf(masterId);
        }

        public RedisNode.RedisNodeBuilder replicaOf(String masterId) {
            this.node.masterId = masterId;
            return this;
        }

        public RedisNode build() {
            return this.node;
        }
    }

    public enum NodeType {
        MASTER,
        SLAVE;

        private NodeType() {
        }
    }
}
