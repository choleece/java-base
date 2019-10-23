package cn.choleece.base.jvm.hotload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-23 23:26
 **/

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileDefine {

    public static String WATCH_PACKAGE = System.getProperty("user.home") + "/Project/idea/java-base/collection/target/classes/cn/choleece/base/jvm/hotload";

    private String fileName;

    private Long lastDefine;

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.home"));
    }

}
