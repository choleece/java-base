package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @description: Redis 集群节点
 * @author: sf
 * @time: 2019-10-14 18:19
 */
public class RedisClusterNode extends RedisNode {

    private RedisClusterNode.SlotRange slotRange;

    @Nullable
    private RedisClusterNode.LinkState linkState;

    private Set<RedisClusterNode.Flag> flags;

    protected RedisClusterNode() {
        if (this.flags == null) {
            this.flags = Collections.emptySet();
        }

        this.flags = Collections.emptySet();
    }

    public RedisClusterNode(String host, int port) {
        this(host, port, RedisClusterNode.SlotRange.empty());
    }

    public RedisClusterNode(String id) {
        this(RedisClusterNode.SlotRange.empty());
        Assert.notNull(id, "Id must not be null!");
        this.id = id;
    }

    public RedisClusterNode(String host, int port, RedisClusterNode.SlotRange slotRange) {
        super(host, port);
        if (this.flags == null) {
            this.flags = Collections.emptySet();
        }

        Assert.notNull(slotRange, "SlotRange must not be null!");
        this.slotRange = slotRange;
    }

    public RedisClusterNode(RedisClusterNode.SlotRange slotRange) {
        if (this.flags == null) {
            this.flags = Collections.emptySet();
        }

        Assert.notNull(slotRange, "SlotRange must not be null!");
        this.slotRange = slotRange;
    }

    public RedisClusterNode.SlotRange getSlotRange() {
        return this.slotRange;
    }

    public boolean servesSlot(int slot) {
        return this.slotRange.contains(slot);
    }

    @Nullable
    public RedisClusterNode.LinkState getLinkState() {
        return this.linkState;
    }

    public boolean isConnected() {
        return RedisClusterNode.LinkState.CONNECTED.equals(this.linkState);
    }

    public Set<RedisClusterNode.Flag> getFlags() {
        return this.flags == null ? Collections.emptySet() : this.flags;
    }

    public boolean isMarkedAsFail() {
        if (CollectionUtils.isEmpty(this.flags)) {
            return false;
        } else {
            return this.flags.contains(RedisClusterNode.Flag.FAIL) || this.flags.contains(RedisClusterNode.Flag.PFAIL);
        }
    }

    public String toString() {
        return super.toString();
    }

    public static RedisClusterNode.RedisClusterNodeBuilder newRedisClusterNode() {
        return new RedisClusterNode.RedisClusterNodeBuilder();
    }

    public static class RedisClusterNodeBuilder extends RedisNodeBuilder {
        @Nullable
        Set<RedisClusterNode.Flag> flags;
        @Nullable
        RedisClusterNode.LinkState linkState;
        RedisClusterNode.SlotRange slotRange = RedisClusterNode.SlotRange.empty();

        public RedisClusterNodeBuilder() {
        }

        public RedisClusterNode.RedisClusterNodeBuilder listeningAt(String host, int port) {
            super.listeningAt(host, port);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder withName(String name) {
            super.withName(name);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder withId(String id) {
            super.withId(id);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder promotedAs(NodeType nodeType) {
            super.promotedAs(nodeType);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder slaveOf(String masterId) {
            super.slaveOf(masterId);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder replicaOf(String masterId) {
            super.replicaOf(masterId);
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder withFlags(Set<RedisClusterNode.Flag> flags) {
            this.flags = flags;
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder serving(RedisClusterNode.SlotRange range) {
            this.slotRange = range;
            return this;
        }

        public RedisClusterNode.RedisClusterNodeBuilder linkState(RedisClusterNode.LinkState linkState) {
            this.linkState = linkState;
            return this;
        }

        public RedisClusterNode build() {
            RedisNode base = super.build();
            RedisClusterNode node;
            if (base.host != null) {
                node = new RedisClusterNode(base.getHost(), base.getPort(), this.slotRange);
            } else {
                node = new RedisClusterNode(this.slotRange);
            }

            node.id = base.id;
            node.type = base.type;
            node.masterId = base.masterId;
            node.name = base.name;
            node.flags = this.flags;
            node.linkState = this.linkState;
            return node;
        }
    }

    public enum Flag {
        MYSELF("myself"),
        MASTER("master"),
        SLAVE("slave"),
        FAIL("fail"),
        PFAIL("fail?"),
        HANDSHAKE("handshake"),
        NOADDR("noaddr"),
        NOFLAGS("noflags");

        private String raw;

        private Flag(String raw) {
            this.raw = raw;
        }

        public String getRaw() {
            return this.raw;
        }
    }

    public enum LinkState {
        CONNECTED,
        DISCONNECTED;

        private LinkState() {
        }
    }

    public static class SlotRange {
        private final Set<Integer> range;

        public SlotRange(Integer lowerBound, Integer upperBound) {
            Assert.notNull(lowerBound, "LowerBound must not be null!");
            Assert.notNull(upperBound, "UpperBound must not be null!");
            this.range = new LinkedHashSet();

            for(int i = lowerBound; i <= upperBound; ++i) {
                this.range.add(i);
            }

        }

        public SlotRange(Collection<Integer> range) {
            this.range = (Set)(CollectionUtils.isEmpty(range) ? Collections.emptySet() : new LinkedHashSet(range));
        }

        public String toString() {
            return this.range.toString();
        }

        public boolean contains(int slot) {
            return this.range.contains(slot);
        }

        public Set<Integer> getSlots() {
            return Collections.unmodifiableSet(this.range);
        }

        public int[] getSlotsArray() {
            int[] slots = new int[this.range.size()];
            int pos = 0;

            Integer value;
            for(Iterator var3 = this.range.iterator(); var3.hasNext(); slots[pos++] = value) {
                value = (Integer)var3.next();
            }

            return slots;
        }

        public static RedisClusterNode.SlotRange empty() {
            return new RedisClusterNode.SlotRange(Collections.emptySet());
        }
    }
}
