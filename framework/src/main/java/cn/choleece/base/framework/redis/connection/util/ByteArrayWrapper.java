package cn.choleece.base.framework.redis.connection.util;

import java.util.Arrays;

/**
 * @description: 字节数组适配
 * @author: sf
 * @time: 2019-10-14 18:06
 */
public class ByteArrayWrapper {

    private final byte[] array;
    private final int hashCode;

    public ByteArrayWrapper(byte[] array) {
        this.array = array;
        this.hashCode = Arrays.hashCode(array);
    }

    public boolean equals(Object obj) {
        return obj instanceof ByteArrayWrapper ? Arrays.equals(this.array, ((ByteArrayWrapper)obj).array) : false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public byte[] getArray() {
        return this.array;
    }

}
