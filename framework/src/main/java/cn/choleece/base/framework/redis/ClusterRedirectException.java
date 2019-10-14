package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataRetrievalFailureException;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:46
 **/
public class ClusterRedirectException extends DataRetrievalFailureException {
    private static final long serialVersionUID = -857075813794333965L;
    private final int slot;
    private final String host;
    private final int port;

    public ClusterRedirectException(int slot, String targetHost, int targetPort, Throwable e) {
        super(String.format("Redirect: slot %s to %s:%s.", slot, targetHost, targetPort), e);
        this.slot = slot;
        this.host = targetHost;
        this.port = targetPort;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getTargetHost() {
        return this.host;
    }

    public int getTargetPort() {
        return this.port;
    }
}
