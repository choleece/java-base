package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.redis.connection.RedisClusterNode;
import cn.choleece.base.framework.redis.connection.RedisServer;
import cn.choleece.base.framework.redis.connection.RedisZSetCommands;
import cn.choleece.base.framework.redis.connection.convert.*;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.Tuple;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:24
 */
public abstract class JedisConverters extends Converters {

    private static final Converter<String, byte[]> STRING_TO_BYTES;
    private static final ListConverter<String, byte[]> STRING_LIST_TO_BYTE_LIST;
    private static final SetConverter<String, byte[]> STRING_SET_TO_BYTE_SET;
    private static final MapConverter<String, byte[]> STRING_MAP_TO_BYTE_MAP;
    private static final SetConverter<Tuple, RedisZSetCommands.Tuple> TUPLE_SET_TO_TUPLE_SET;
    private static final Converter<Exception, DataAccessException> EXCEPTION_CONVERTER = new JedisExceptionConverter();
    private static final Converter<String[], List<RedisClientInfo>> STRING_TO_CLIENT_INFO_CONVERTER = new StringToRedisClientInfoConverter();
    private static final Converter<Tuple, RedisZSetCommands.Tuple> TUPLE_CONVERTER;
    private static final ListConverter<Tuple, RedisZSetCommands.Tuple> TUPLE_LIST_TO_TUPLE_LIST_CONVERTER;
    private static final Converter<Object, RedisClusterNode> OBJECT_TO_CLUSTER_NODE_CONVERTER;
    private static final Converter<Expiration, byte[]> EXPIRATION_TO_COMMAND_OPTION_CONVERTER;
    private static final Converter<SetOption, byte[]> SET_OPTION_TO_COMMAND_OPTION_CONVERTER;
    private static final Converter<List<String>, Long> STRING_LIST_TO_TIME_CONVERTER;
    private static final Converter<GeoCoordinate, Point> GEO_COORDINATE_TO_POINT_CONVERTER;
    private static final ListConverter<GeoCoordinate, Point> LIST_GEO_COORDINATE_TO_POINT_CONVERTER;
    private static final Converter<byte[], String> BYTES_TO_STRING_CONVERTER = (source) -> {
        return source == null ? null : SafeEncoder.encode(source);
    };
    private static final ListConverter<byte[], String> BYTES_LIST_TO_STRING_LIST_CONVERTER;
    private static final ListConverter<byte[], Long> BYTES_LIST_TO_LONG_LIST_CONVERTER;
    private static final Converter<BitFieldSubCommands, List<byte[]>> BITFIELD_COMMAND_ARGUMENT_CONVERTER;
    public static final byte[] PLUS_BYTES;
    public static final byte[] MINUS_BYTES;
    public static final byte[] POSITIVE_INFINITY_BYTES;
    public static final byte[] NEGATIVE_INFINITY_BYTES;
    private static final byte[] EX;
    private static final byte[] PX;
    private static final byte[] NX;
    private static final byte[] XX;

    public JedisConverters() {
    }

    public static Converter<String, byte[]> stringToBytes() {
        return STRING_TO_BYTES;
    }

    public static ListConverter<Tuple, RedisZSetCommands.Tuple> tuplesToTuples() {
        return TUPLE_LIST_TO_TUPLE_LIST_CONVERTER;
    }

    public static ListConverter<String, byte[]> stringListToByteList() {
        return STRING_LIST_TO_BYTE_LIST;
    }

    public static SetConverter<String, byte[]> stringSetToByteSet() {
        return STRING_SET_TO_BYTE_SET;
    }

    public static MapConverter<String, byte[]> stringMapToByteMap() {
        return STRING_MAP_TO_BYTE_MAP;
    }

    public static SetConverter<Tuple, org.springframework.data.redis.connection.RedisZSetCommands.Tuple> tupleSetToTupleSet() {
        return TUPLE_SET_TO_TUPLE_SET;
    }

    public static Converter<Exception, DataAccessException> exceptionConverter() {
        return EXCEPTION_CONVERTER;
    }

    public static String[] toStrings(byte[][] source) {
        String[] result = new String[source.length];

        for(int i = 0; i < source.length; ++i) {
            result[i] = SafeEncoder.encode(source[i]);
        }

        return result;
    }

    public static Set<org.springframework.data.redis.connection.RedisZSetCommands.Tuple> toTupleSet(Set<Tuple> source) {
        return TUPLE_SET_TO_TUPLE_SET.convert(source);
    }

    public static Map<byte[], Double> toTupleMap(Set<org.springframework.data.redis.connection.RedisZSetCommands.Tuple> tuples) {
        Assert.notNull(tuples, "Tuple set must not be null!");
        Map<byte[], Double> args = new LinkedHashMap(tuples.size(), 1.0F);
        Set<Double> scores = new HashSet(tuples.size(), 1.0F);
        boolean isAtLeastJedis24 = JedisVersionUtil.atLeastJedis24();

        org.springframework.data.redis.connection.RedisZSetCommands.Tuple tuple;
        for(Iterator var4 = tuples.iterator(); var4.hasNext(); args.put(tuple.getValue(), tuple.getScore())) {
            tuple = (org.springframework.data.redis.connection.RedisZSetCommands.Tuple)var4.next();
            if (!isAtLeastJedis24) {
                if (scores.contains(tuple.getScore())) {
                    throw new UnsupportedOperationException("Bulk add of multiple elements with the same score is not supported. Add the elements individually.");
                }

                scores.add(tuple.getScore());
            }
        }

        return args;
    }

    public static byte[] toBytes(Integer source) {
        return String.valueOf(source).getBytes();
    }

    public static byte[] toBytes(Long source) {
        return String.valueOf(source).getBytes();
    }

    public static byte[] toBytes(Double source) {
        return toBytes(String.valueOf(source));
    }

    public static byte[] toBytes(String source) {
        return (byte[])STRING_TO_BYTES.convert(source);
    }

    @Nullable
    public static String toString(@Nullable byte[] source) {
        return source == null ? null : SafeEncoder.encode(source);
    }

    public static ValueEncoding toEncoding(@Nullable byte[] source) {
        return ValueEncoding.of(toString(source));
    }

    public static RedisClusterNode toNode(Object source) {
        return (RedisClusterNode)OBJECT_TO_CLUSTER_NODE_CONVERTER.convert(source);
    }

    public static List<RedisClientInfo> toListOfRedisClientInformation(String source) {
        return !StringUtils.hasText(source) ? Collections.emptyList() : (List)STRING_TO_CLIENT_INFO_CONVERTER.convert(source.split("\\r?\\n"));
    }

    public static List<RedisServer> toListOfRedisServer(List<Map<String, String>> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        } else {
            List<RedisServer> sentinels = new ArrayList();
            Iterator var2 = source.iterator();

            while(var2.hasNext()) {
                Map<String, String> info = (Map)var2.next();
                sentinels.add(RedisServer.newServerFrom(Converters.toProperties(info)));
            }

            return sentinels;
        }
    }

    public static DataAccessException toDataAccessException(Exception ex) {
        return (DataAccessException)EXCEPTION_CONVERTER.convert(ex);
    }

    public static LIST_POSITION toListPosition(Position source) {
        Assert.notNull(source, "list positions are mandatory");
        return Position.AFTER.equals(source) ? LIST_POSITION.AFTER : LIST_POSITION.BEFORE;
    }

    public static byte[][] toByteArrays(Map<byte[], byte[]> source) {
        byte[][] result = new byte[source.size() * 2][];
        int index = 0;

        Map.Entry entry;
        for(Iterator var3 = source.entrySet().iterator(); var3.hasNext(); result[index++] = (byte[])entry.getValue()) {
            entry = (Map.Entry)var3.next();
            result[index++] = (byte[])entry.getKey();
        }

        return result;
    }

    @Nullable
    public static SortingParams toSortingParams(@Nullable SortParameters params) {
        SortingParams jedisParams = null;
        if (params != null) {
            jedisParams = new SortingParams();
            byte[] byPattern = params.getByPattern();
            if (byPattern != null) {
                jedisParams.by(params.getByPattern());
            }

            byte[][] getPattern = params.getGetPattern();
            if (getPattern != null) {
                jedisParams.get(getPattern);
            }

            Range limit = params.getLimit();
            if (limit != null) {
                jedisParams.limit((int)limit.getStart(), (int)limit.getCount());
            }

            Order order = params.getOrder();
            if (order != null && order.equals(Order.DESC)) {
                jedisParams.desc();
            }

            Boolean isAlpha = params.isAlphabetic();
            if (isAlpha != null && isAlpha) {
                jedisParams.alpha();
            }
        }

        return jedisParams;
    }

    public static BitOP toBitOp(BitOperation bitOp) {
        switch(bitOp) {
            case AND:
                return BitOP.AND;
            case OR:
                return BitOP.OR;
            case NOT:
                return BitOP.NOT;
            case XOR:
                return BitOP.XOR;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte[] boundaryToBytesForZRange(@Nullable Boundary boundary, byte[] defaultValue) {
        return boundary != null && boundary.getValue() != null ? boundaryToBytes(boundary, new byte[0], toBytes("(")) : defaultValue;
    }

    public static byte[] boundaryToBytesForZRangeByLex(@Nullable Boundary boundary, byte[] defaultValue) {
        return boundary != null && boundary.getValue() != null ? boundaryToBytes(boundary, toBytes("["), toBytes("(")) : defaultValue;
    }

    public static byte[] toSetCommandExPxArgument(Expiration expiration) {
        return (byte[])EXPIRATION_TO_COMMAND_OPTION_CONVERTER.convert(expiration);
    }

    public static byte[] toSetCommandNxXxArgument(SetOption option) {
        return (byte[])SET_OPTION_TO_COMMAND_OPTION_CONVERTER.convert(option);
    }

    private static byte[] boundaryToBytes(Boundary boundary, byte[] inclPrefix, byte[] exclPrefix) {
        byte[] prefix = boundary.isIncluding() ? inclPrefix : exclPrefix;
        byte[] value = null;
        byte[] value;
        if (boundary.getValue() instanceof byte[]) {
            value = (byte[])((byte[])boundary.getValue());
        } else if (boundary.getValue() instanceof Double) {
            value = toBytes((Double)boundary.getValue());
        } else if (boundary.getValue() instanceof Long) {
            value = toBytes((Long)boundary.getValue());
        } else if (boundary.getValue() instanceof Integer) {
            value = toBytes((Integer)boundary.getValue());
        } else {
            if (!(boundary.getValue() instanceof String)) {
                throw new IllegalArgumentException(String.format("Cannot convert %s to binary format", boundary.getValue()));
            }

            value = toBytes((String)boundary.getValue());
        }

        ByteBuffer buffer = ByteBuffer.allocate(prefix.length + value.length);
        buffer.put(prefix);
        buffer.put(value);
        return buffer.array();
    }

    public static ScanParams toScanParams(ScanOptions options) {
        ScanParams sp = new ScanParams();
        if (!options.equals(ScanOptions.NONE)) {
            if (options.getCount() != null) {
                sp.count(options.getCount().intValue());
            }

            if (StringUtils.hasText(options.getPattern())) {
                sp.match(options.getPattern());
            }
        }

        return sp;
    }

    static Converter<List<String>, Long> toTimeConverter() {
        return STRING_LIST_TO_TIME_CONVERTER;
    }

    public static List<String> toStrings(List<byte[]> source) {
        return BYTES_LIST_TO_STRING_LIST_CONVERTER.convert(source);
    }

    public static ListConverter<byte[], String> bytesListToStringListConverter() {
        return BYTES_LIST_TO_STRING_LIST_CONVERTER;
    }

    public static ListConverter<byte[], Long> getBytesListToLongListConverter() {
        return BYTES_LIST_TO_LONG_LIST_CONVERTER;
    }

    public static ListConverter<GeoCoordinate, Point> geoCoordinateToPointConverter() {
        return LIST_GEO_COORDINATE_TO_POINT_CONVERTER;
    }

    public static Converter<List<GeoRadiusResponse>, GeoResults<GeoLocation<byte[]>>> geoRadiusResponseToGeoResultsConverter(Metric metric) {
        return JedisConverters.GeoResultsConverterFactory.INSTANCE.forMetric(metric);
    }

    public static GeoUnit toGeoUnit(Metric metric) {
        Metric metricToUse = metric != null && !ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric) ? metric : DistanceUnit.METERS;
        return (GeoUnit)ObjectUtils.caseInsensitiveValueOf(GeoUnit.values(), ((Metric)metricToUse).getAbbreviation());
    }

    public static GeoCoordinate toGeoCoordinate(Point source) {
        return source == null ? null : new GeoCoordinate(source.getX(), source.getY());
    }

    public static GeoRadiusParam toGeoRadiusParam(GeoRadiusCommandArgs source) {
        GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();
        if (source == null) {
            return param;
        } else {
            if (source.hasFlags()) {
                Iterator var2 = source.getFlags().iterator();

                while(var2.hasNext()) {
                    Flag flag = (Flag)var2.next();
                    switch(flag) {
                        case WITHCOORD:
                            param.withCoord();
                            break;
                        case WITHDIST:
                            param.withDist();
                    }
                }
            }

            if (source.hasSortDirection()) {
                switch(source.getSortDirection()) {
                    case ASC:
                        param.sortAscending();
                        break;
                    case DESC:
                        param.sortDescending();
                }
            }

            if (source.hasLimit()) {
                param.count(source.getLimit().intValue());
            }

            return param;
        }
    }

    public static byte[][] toBitfieldCommandArguments(BitFieldSubCommands bitfieldOperation) {
        List<byte[]> tmp = (List)BITFIELD_COMMAND_ARGUMENT_CONVERTER.convert(bitfieldOperation);
        return (byte[][])tmp.toArray(new byte[tmp.size()][]);
    }

    static {
        BYTES_LIST_TO_STRING_LIST_CONVERTER = new ListConverter(BYTES_TO_STRING_CONVERTER);
        STRING_TO_BYTES = (source) -> {
            return source == null ? null : SafeEncoder.encode(source);
        };
        STRING_LIST_TO_BYTE_LIST = new ListConverter(STRING_TO_BYTES);
        STRING_SET_TO_BYTE_SET = new SetConverter(STRING_TO_BYTES);
        STRING_MAP_TO_BYTE_MAP = new MapConverter(STRING_TO_BYTES);
        TUPLE_CONVERTER = (source) -> {
            return source != null ? new DefaultTuple(source.getBinaryElement(), source.getScore()) : null;
        };
        TUPLE_SET_TO_TUPLE_SET = new SetConverter(TUPLE_CONVERTER);
        TUPLE_LIST_TO_TUPLE_LIST_CONVERTER = new ListConverter(TUPLE_CONVERTER);
        PLUS_BYTES = toBytes("+");
        MINUS_BYTES = toBytes("-");
        POSITIVE_INFINITY_BYTES = toBytes("+inf");
        NEGATIVE_INFINITY_BYTES = toBytes("-inf");
        OBJECT_TO_CLUSTER_NODE_CONVERTER = (infos) -> {
            List<Object> values = (List)infos;
            SlotRange range = new SlotRange(((Number)values.get(0)).intValue(), ((Number)values.get(1)).intValue());
            List<Object> nodeInfo = (List)values.get(2);
            return new RedisClusterNode(toString((byte[])((byte[])nodeInfo.get(0))), ((Number)nodeInfo.get(1)).intValue(), range);
        };
        EX = toBytes("EX");
        PX = toBytes("PX");
        EXPIRATION_TO_COMMAND_OPTION_CONVERTER = new Converter<Expiration, byte[]>() {
            public byte[] convert(Expiration source) {
                if (source != null && !source.isPersistent()) {
                    return ObjectUtils.nullSafeEquals(TimeUnit.MILLISECONDS, source.getTimeUnit()) ? JedisConverters.PX : JedisConverters.EX;
                } else {
                    return new byte[0];
                }
            }
        };
        NX = toBytes("NX");
        XX = toBytes("XX");
        SET_OPTION_TO_COMMAND_OPTION_CONVERTER = new Converter<SetOption, byte[]>() {
            public byte[] convert(SetOption source) {
                switch(source) {
                    case UPSERT:
                        return new byte[0];
                    case SET_IF_ABSENT:
                        return JedisConverters.NX;
                    case SET_IF_PRESENT:
                        return JedisConverters.XX;
                    default:
                        throw new IllegalArgumentException(String.format("Invalid argument %s for SetOption.", source));
                }
            }
        };
        STRING_LIST_TO_TIME_CONVERTER = (source) -> {
            Assert.notEmpty(source, "Received invalid result from server. Expected 2 items in collection.");
            Assert.isTrue(source.size() == 2, "Received invalid nr of arguments from redis server. Expected 2 received " + source.size());
            return toTimeMillis((String)source.get(0), (String)source.get(1));
        };
        GEO_COORDINATE_TO_POINT_CONVERTER = (geoCoordinate) -> {
            return geoCoordinate != null ? new Point(geoCoordinate.getLongitude(), geoCoordinate.getLatitude()) : null;
        };
        LIST_GEO_COORDINATE_TO_POINT_CONVERTER = new ListConverter(GEO_COORDINATE_TO_POINT_CONVERTER);
        BYTES_LIST_TO_LONG_LIST_CONVERTER = new ListConverter(new Converter<byte[], Long>() {
            public Long convert(byte[] source) {
                return Long.valueOf(JedisConverters.toString(source));
            }
        });
        BITFIELD_COMMAND_ARGUMENT_CONVERTER = new Converter<BitFieldSubCommands, List<byte[]>>() {
            public List<byte[]> convert(BitFieldSubCommands source) {
                if (source == null) {
                    return Collections.emptyList();
                } else {
                    List<byte[]> args = new ArrayList(source.getSubCommands().size() * 4);
                    Iterator var3 = source.getSubCommands().iterator();

                    while(var3.hasNext()) {
                        BitFieldSubCommand command = (BitFieldSubCommand)var3.next();
                        if (command instanceof BitFieldIncrBy) {
                            Overflow overflow = ((BitFieldIncrBy)command).getOverflow();
                            if (overflow != null) {
                                args.add(JedisConverters.toBytes("OVERFLOW"));
                                args.add(JedisConverters.toBytes(overflow.name()));
                            }
                        }

                        args.add(JedisConverters.toBytes(command.getCommand()));
                        args.add(JedisConverters.toBytes(command.getType().asString()));
                        args.add(JedisConverters.toBytes(command.getOffset().asString()));
                        if (command instanceof BitFieldSet) {
                            args.add(JedisConverters.toBytes(((BitFieldSet)command).getValue()));
                        } else if (command instanceof BitFieldIncrBy) {
                            args.add(JedisConverters.toBytes(((BitFieldIncrBy)command).getValue()));
                        }
                    }

                    return args;
                }
            }
        };
    }

    enum GeoResultConverterFactory {
        INSTANCE;

        private GeoResultConverterFactory() {
        }

        Converter<GeoRadiusResponse, GeoResult<GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new JedisConverters.GeoResultConverterFactory.GeoResultConverter(metric);
        }

        private static class GeoResultConverter implements Converter<GeoRadiusResponse, GeoResult<GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultConverter(Metric metric) {
                this.metric = metric;
            }

            public GeoResult<GeoLocation<byte[]>> convert(GeoRadiusResponse source) {
                Point point = (Point)JedisConverters.GEO_COORDINATE_TO_POINT_CONVERTER.convert(source.getCoordinate());
                return new GeoResult(new GeoLocation(source.getMember(), point), new Distance(source.getDistance(), this.metric));
            }
        }
    }

    enum GeoResultsConverterFactory {
        INSTANCE;

        private GeoResultsConverterFactory() {
        }

        Converter<List<GeoRadiusResponse>, GeoResults<GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new JedisConverters.GeoResultsConverterFactory.GeoResultsConverter((Metric)(metric != null && !ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric) ? metric : DistanceUnit.METERS));
        }

        private static class GeoResultsConverter implements Converter<List<GeoRadiusResponse>, GeoResults<GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultsConverter(Metric metric) {
                this.metric = metric;
            }

            public GeoResults<GeoLocation<byte[]>> convert(List<GeoRadiusResponse> source) {
                List<GeoResult<GeoLocation<byte[]>>> results = new ArrayList(source.size());
                Converter<GeoRadiusResponse, GeoResult<GeoLocation<byte[]>>> converter = JedisConverters.GeoResultConverterFactory.INSTANCE.forMetric(this.metric);
                Iterator var4 = source.iterator();

                while(var4.hasNext()) {
                    GeoRadiusResponse result = (GeoRadiusResponse)var4.next();
                    results.add(converter.convert(result));
                }

                return new GeoResults(results, this.metric);
            }
        }
    }

}