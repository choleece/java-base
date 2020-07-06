package cn.choleece.base.md.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choleece
 * @Description: kafka 消息类
 * @Date 2020-07-05 16:24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleMessage {

    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息内容
     */
    private String msg;

}
