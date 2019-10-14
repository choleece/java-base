package cn.choleece.base.framework.redis.connection.convert;

import cn.choleece.base.framework.redis.connection.DataType;
import cn.choleece.base.framework.redis.connection.RedisClusterNode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:25
 */
public abstract class Converters {

    private static final byte[] ONE = new byte[]{49};

    private static final byte[] ZERO = new byte[]{48};

    private static final String CLUSTER_NODES_LINE_SEPARATOR = "\n";

    private static final Converter<String, Properties> STRING_TO_PROPS = new StringToPropertiesConverter();

    private static final Converter<Long, Boolean> LONG_TO_BOOLEAN = new LongToBooleanConverter();

    private static final Converter<String, DataType> STRING_TO_DATA_TYPE = new StringToDataTypeConverter();

    private static final Converter<Map<?, ?>, Properties> MAP_TO_PROPERTIES;

    private static final Converter<String, RedisClusterNode> STRING_TO_CLUSTER_NODE_CONVERTER;

    private static final Converter<List<String>, Properties> STRING_LIST_TO_PROPERTIES_CONVERTER;

    private static final Map<String, RedisClusterNode.Flag> flagLookupMap;

    public Converters() {
    }

    public static Boolean stringToBoolean(String s) {
        return (Boolean)stringToBooleanConverter().convert(s);
    }

    public static Converter<String, Boolean> stringToBooleanConverter() {
        return (source) -> {
            return ObjectUtils.nullSafeEquals("OK", source);
        };
    }

    public static Converter<String, Properties> stringToProps() {
        return STRING_TO_PROPS;
    }

    public static Converter<Long, Boolean> longToBoolean() {
        return LONG_TO_BOOLEAN;
    }

    public static Converter<String, DataType> stringToDataType() {
        return STRING_TO_DATA_TYPE;
    }

    public static Properties toProperties(String source) {
        return (Properties)STRING_TO_PROPS.convert(source);
    }

    public static Properties toProperties(Map<?, ?> source) {
        Properties properties = (Properties)MAP_TO_PROPERTIES.convert(source);
        return properties != null ? properties : new Properties();
    }

    public static Boolean toBoolean(Long source) {
        return (Boolean)LONG_TO_BOOLEAN.convert(source);
    }

    public static DataType toDataType(String source) {
        return (DataType)STRING_TO_DATA_TYPE.convert(source);
    }

    public static byte[] toBit(Boolean source) {
        return source ? ONE : ZERO;
    }

    protected static RedisClusterNode toClusterNode(String clusterNodesLine) {
        return (RedisClusterNode)STRING_TO_CLUSTER_NODE_CONVERTER.convert(clusterNodesLine);
    }

    public static Set<RedisClusterNode> toSetOfRedisClusterNodes(Collection<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            return Collections.emptySet();
        } else {
            Set<RedisClusterNode> nodes = new LinkedHashSet(lines.size());
            Iterator var2 = lines.iterator();

            while(var2.hasNext()) {
                String line = (String)var2.next();
                nodes.add(toClusterNode(line));
            }

            return nodes;
        }
    }

    public static Set<RedisClusterNode> toSetOfRedisClusterNodes(String clusterNodes) {
        if (StringUtils.isEmpty(clusterNodes)) {
            return Collections.emptySet();
        } else {
            String[] lines = clusterNodes.split("\n");
            return toSetOfRedisClusterNodes((Collection)Arrays.asList(lines));
        }
    }

    public static List<Object> toObjects(Set<Tuple> tuples) {
        List<Object> tupleArgs = new ArrayList(tuples.size() * 2);
        Iterator var2 = tuples.iterator();

        while(var2.hasNext()) {
            Tuple tuple = (Tuple)var2.next();
            tupleArgs.add(tuple.getScore());
            tupleArgs.add(tuple.getValue());
        }

        return tupleArgs;
    }

    public static Long toTimeMillis(String seconds, String microseconds) {
        return (Long) NumberUtils.parseNumber(seconds, Long.class) * 1000L + (Long)NumberUtils.parseNumber(microseconds, Long.class) / 1000L;
    }

    public static long secondsToTimeUnit(long seconds, TimeUnit targetUnit) {
        Assert.notNull(targetUnit, "TimeUnit must not be null!");
        return seconds > 0L ? targetUnit.convert(seconds, TimeUnit.SECONDS) : seconds;
    }

    public static Converter<Long, Long> secondsToTimeUnit(TimeUnit timeUnit) {
        return (seconds) -> {
            return secondsToTimeUnit(seconds, timeUnit);
        };
    }

    public static long millisecondsToTimeUnit(long milliseconds, TimeUnit targetUnit) {
        Assert.notNull(targetUnit, "TimeUnit must not be null!");
        return milliseconds > 0L ? targetUnit.convert(milliseconds, TimeUnit.MILLISECONDS) : milliseconds;
    }

    public static Converter<Long, Long> millisecondsToTimeUnit(TimeUnit timeUnit) {
        return (seconds) -> {
            return millisecondsToTimeUnit(seconds, timeUnit);
        };
    }

    public static <V> Converter<GeoResults<GeoLocation<byte[]>>, GeoResults<GeoLocation<V>>> deserializingGeoResultsConverter(RedisSerializer<V> serializer) {
        return new Converters.DeserializingGeoResultsConverter(serializer);
    }

    public static Converter<Double, Distance> distanceConverterForMetric(Metric metric) {
        return Converters.DistanceConverterFactory.INSTANCE.forMetric(metric);
    }

    public static Properties toProperties(List<String> input) {
        return (Properties)STRING_LIST_TO_PROPERTIES_CONVERTER.convert(input);
    }

    public static Converter<List<String>, Properties> listToPropertiesConverter() {
        return STRING_LIST_TO_PROPERTIES_CONVERTER;
    }

    public static <K, V> Converter<Map<K, V>, Properties> mapToPropertiesConverter() {
        return MAP_TO_PROPERTIES;
    }

    @Nullable
    public static Duration secondsToDuration(@Nullable Long seconds) {
        return seconds != null ? Duration.ofSeconds(seconds) : null;
    }

    static {
        MAP_TO_PROPERTIES = MapToPropertiesConverter.INSTANCE;
        flagLookupMap = new LinkedHashMap(RedisClusterNode.Flag.values().length, 1.0F);
        RedisClusterNode.Flag[] var0 = RedisClusterNode.Flag.values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            RedisClusterNode.Flag flag = var0[var2];
            flagLookupMap.put(flag.getRaw(), flag);
        }

        STRING_TO_CLUSTER_NODE_CONVERTER = new Converter<String, RedisClusterNode>() {
            static final int ID_INDEX = 0;
            static final int HOST_PORT_INDEX = 1;
            static final int FLAGS_INDEX = 2;
            static final int MASTER_ID_INDEX = 3;
            static final int LINK_STATE_INDEX = 7;
            static final int SLOTS_INDEX = 8;

            public RedisClusterNode convert(String source) {
                String[] args = source.split(" ");
                String[] hostAndPort = StringUtils.split(args[1], ":");
                Assert.notNull(hostAndPort, "CusterNode information does not define host and port!");
                RedisClusterNode.SlotRange range = this.parseSlotRange(args);
                Set<RedisClusterNode.Flag> flags = this.parseFlags(args);
                String portPart = hostAndPort[1];
                if (portPart.contains("@")) {
                    portPart = portPart.substring(0, portPart.indexOf(64));
                }

                RedisClusterNode.RedisClusterNodeBuilder nodeBuilder = RedisClusterNode.newRedisClusterNode().listeningAt(hostAndPort[0], Integer.valueOf(portPart)).withId(args[0]).promotedAs(flags.contains(Flag.MASTER) ? NodeType.MASTER : NodeType.SLAVE).serving(range).withFlags(flags).linkState(this.parseLinkState(args));
                if (!args[3].isEmpty() && !args[3].startsWith("-")) {
                    nodeBuilder.slaveOf(args[3]);
                }

                return nodeBuilder.build();
            }

            private Set<RedisClusterNode.Flag> parseFlags(String[] args) {
                String raw = args[2];
                Set<RedisClusterNode.Flag> flags = new LinkedHashSet(8, 1.0F);
                if (StringUtils.hasText(raw)) {
                    String[] var4 = raw.split(",");
                    int var5 = var4.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        String flag = var4[var6];
                        flags.add(Converters.flagLookupMap.get(flag));
                    }
                }

                return flags;
            }

            private RedisClusterNode.LinkState parseLinkState(String[] args) {
                String raw = args[7];
                return StringUtils.hasText(raw) ? RedisClusterNode.LinkState.valueOf(raw.toUpperCase()) : LinkState.DISCONNECTED;
            }

            private RedisClusterNode.SlotRange parseSlotRange(String[] args) {
                Set<Integer> slots = new LinkedHashSet();

                for(int i = 8; i < args.length; ++i) {
                    String raw = args[i];
                    if (!raw.startsWith("[")) {
                        if (raw.contains("-")) {
                            String[] slotRange = StringUtils.split(raw, "-");
                            if (slotRange != null) {
                                int from = Integer.valueOf(slotRange[0]);
                                int to = Integer.valueOf(slotRange[1]);

                                for(int slot = from; slot <= to; ++slot) {
                                    slots.add(slot);
                                }
                            }
                        } else {
                            slots.add(Integer.valueOf(raw));
                        }
                    }
                }

                return new RedisClusterNode.SlotRange(slots);
            }
        };
        STRING_LIST_TO_PROPERTIES_CONVERTER = (input) -> {
            Assert.notNull(input, "Input list must not be null!");
            Assert.isTrue(input.size() % 2 == 0, "Input list must contain an even number of entries!");
            Properties properties = new Properties();

            for(int i = 0; i < input.size(); i += 2) {
                properties.setProperty((String)input.get(i), (String)input.get(i + 1));
            }

            return properties;
        };
    }

    static class DeserializingGeoResultsConverter<V> implements Converter<GeoResults<GeoLocation<byte[]>>, GeoResults<GeoLocation<V>>> {
        final RedisSerializer<V> serializer;

        public GeoResults<GeoLocation<V>> convert(GeoResults<GeoLocation<byte[]>> source) {
            List<GeoResult<GeoLocation<V>>> values = new ArrayList(source.getContent().size());
            Iterator var3 = source.getContent().iterator();

            while(var3.hasNext()) {
                GeoResult<GeoLocation<byte[]>> value = (GeoResult)var3.next();
                values.add(new GeoResult(new GeoLocation(this.serializer.deserialize((byte[])((GeoLocation)value.getContent()).getName()), ((GeoLocation)value.getContent()).getPoint()), value.getDistance()));
            }

            return new GeoResults(values, source.getAverageDistance().getMetric());
        }

        public DeserializingGeoResultsConverter(RedisSerializer<V> serializer) {
            this.serializer = serializer;
        }
    }

    enum DistanceConverterFactory {
        INSTANCE;

        private DistanceConverterFactory() {
        }

        Converters.DistanceConverterFactory.DistanceConverter forMetric(@Nullable Metric metric) {
            return new Converters.DistanceConverterFactory.DistanceConverter((Metric)(metric != null && !ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric) ? metric : DistanceUnit.METERS));
        }

        static class DistanceConverter implements Converter<Double, Distance> {
            private Metric metric;

            DistanceConverter(@Nullable Metric metric) {
                this.metric = (Metric)(metric != null && !ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric) ? metric : DistanceUnit.METERS);
            }

            public Distance convert(Double source) {
                return new Distance(source, this.metric);
            }
        }
    }
}
