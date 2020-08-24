package cn.choleece.base.jdk.io.buffer;

/**
 * @author choleece
 * @Description: Buffer 测试
 * @Date 2019-10-27 00:04
 *
 * capacity: 容量，缓冲区能够容纳的数据元素的最大数量
 * limit: 上界，缓冲区第一个不能被读或写的元素
 * position: 位置，下一个要被读或写的元素的索引
 * mark: 标记，一个备忘位置，可以通过mark()去设定position = mark, 标记在为设定钱是undefined的
 *
 * 0 <= mark <= position <= limit <= capacity
 **/
public class BufferTest {

    public static void main(String[] args) {
    }

}
