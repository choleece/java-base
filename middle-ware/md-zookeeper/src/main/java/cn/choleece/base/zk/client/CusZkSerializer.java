package cn.choleece.base.zk.client;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.Charset;

/**
 * @author choleece
 * @Description: zk 自定义序列化
 * @Date 2019-10-17 22:06
 **/
public class CusZkSerializer implements ZkSerializer {

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, Charset.forName("UTF-8"));
    }

    @Override
    public byte[] serialize(Object obj) throws ZkMarshallingError {
        return String.valueOf(obj).getBytes(Charset.forName("UTF-8"));
    }
}
