package cn.choleece.base.framework.redis.connection.jedis;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.*;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 20:53
 **/
class JedisClientUtils {

    private static final Field CLIENT_FIELD = ReflectionUtils.findField(BinaryJedis.class, "client", Client.class);
    private static final Method SEND_COMMAND;
    private static final Method GET_RESPONSE;
    private static final Method PROTOCOL_SEND_COMMAND;
    private static final Set<String> KNOWN_COMMANDS;
    private static final Builder<Object> OBJECT_BUILDER;

    JedisClientUtils() {
    }

    static <T> T execute(String command, byte[][] keys, byte[][] args, Supplier<Jedis> jedis) {
        return execute(command, keys, args, jedis, (it) -> (T)it.getOne());
    }

    static <T> T execute(String command, byte[][] keys, byte[][] args, Supplier<Jedis> jedis, Function<Client, T> responseMapper) {
        byte[][] commandArgs = getCommandArguments(keys, args);
        Client client = sendCommand(command, commandArgs, (Jedis)jedis.get());
        return responseMapper.apply(client);
    }

    static Client sendCommand(String command, byte[][] args, Jedis jedis) {
        Client client = retrieveClient(jedis);
        sendCommand(client, command, args);
        return client;
    }

    private static void sendCommand(Client client, String command, byte[][] args) {
        if (isKnownCommand(command)) {
            ReflectionUtils.invokeMethod(SEND_COMMAND, client, new Object[]{Protocol.Command.valueOf(command.trim().toUpperCase()), args});
        } else {
            sendProtocolCommand(client, command, args);
        }

    }

    private static void sendProtocolCommand(Client client, String command, byte[][] args) {
        DirectFieldAccessor dfa = new DirectFieldAccessor(client);
        client.connect();
        RedisOutputStream os = (RedisOutputStream)dfa.getPropertyValue("outputStream");
        ReflectionUtils.invokeMethod(PROTOCOL_SEND_COMMAND, (Object)null, new Object[]{os, SafeEncoder.encode(command), args});
        Integer pipelinedCommands = (Integer)dfa.getPropertyValue("pipelinedCommands");
        dfa.setPropertyValue("pipelinedCommands", pipelinedCommands + 1);
    }

    private static boolean isKnownCommand(String command) {
        return KNOWN_COMMANDS.contains(command);
    }

    private static byte[][] getCommandArguments(byte[][] keys, byte[][] args) {
        if (keys.length == 0) {
            return args;
        } else if (args.length == 0) {
            return keys;
        } else {
            byte[][] commandArgs = new byte[keys.length + args.length][];
            System.arraycopy(keys, 0, commandArgs, 0, keys.length);
            System.arraycopy(args, 0, commandArgs, keys.length, args.length);
            return commandArgs;
        }
    }

    static boolean isInMulti(Jedis jedis) {
        return retrieveClient(jedis).isInMulti();
    }

    static Response<Object> getResponse(Object target) {
        return (Response)ReflectionUtils.invokeMethod(GET_RESPONSE, target, new Object[]{OBJECT_BUILDER});
    }

    private static Client retrieveClient(Jedis jedis) {
        return (Client)ReflectionUtils.getField(CLIENT_FIELD, jedis);
    }

    static {
        ReflectionUtils.makeAccessible(CLIENT_FIELD);
        PROTOCOL_SEND_COMMAND = ReflectionUtils.findMethod(Protocol.class, "sendCommand", new Class[]{RedisOutputStream.class, byte[].class, byte[][].class});
        ReflectionUtils.makeAccessible(PROTOCOL_SEND_COMMAND);

        try {
            Class<?> commandType = ClassUtils.isPresent("redis.clients.jedis.ProtocolCommand", (ClassLoader)null) ? ClassUtils.forName("redis.clients.jedis.ProtocolCommand", (ClassLoader)null) : ClassUtils.forName("redis.clients.jedis.Protocol$Command", (ClassLoader)null);
            SEND_COMMAND = ReflectionUtils.findMethod(Connection.class, "sendCommand", new Class[]{commandType, byte[][].class});
        } catch (Exception var1) {
            throw new NoClassDefFoundError("Could not find required flavor of command required by 'redis.clients.jedis.Connection#sendCommand'.");
        }

        ReflectionUtils.makeAccessible(SEND_COMMAND);
        GET_RESPONSE = ReflectionUtils.findMethod(Queable.class, "getResponse", new Class[]{Builder.class});
        ReflectionUtils.makeAccessible(GET_RESPONSE);
        KNOWN_COMMANDS = (Set) Arrays.stream(Protocol.Command.values()).map(Enum::name).collect(Collectors.toSet());
        OBJECT_BUILDER = new Builder<Object>() {

            @Override
            public Object build(Object data) {
                return data;
            }

            @Override
            public String toString() {
                return "Object";
            }
        };
    }
}
