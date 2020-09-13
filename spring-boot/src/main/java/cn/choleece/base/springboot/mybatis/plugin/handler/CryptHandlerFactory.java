package cn.choleece.base.springboot.mybatis.plugin.handler;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;

import java.util.List;
import java.util.Map;

/**
 * @author choleece
 * @Description: crypt handler factory
 * @Date 2020-09-13 14:17
 **/
public class CryptHandlerFactory {

    static final CryptHandler STRING_HANDLER = new StringCryptHandler();
    static final CryptHandler LIST_HANDLER = new ListCryptHandler();
    static final CryptHandler EMPTY_HANDLER = new EmptyCryptHandler();

    public static CryptHandler getCryptHandler(Object obj, CryptField cryptField) {
        // 如果是map不处理
        if (obj == null || CryptUtils.inIgnoreClass(obj.getClass()) || obj instanceof Map) {
            return EMPTY_HANDLER;
        }

        if (obj instanceof String && cryptField == null) {
            return EMPTY_HANDLER;
        }
        if (obj instanceof String) {
            return STRING_HANDLER;
        }

        if (obj instanceof List) {
            return LIST_HANDLER;
        }

        return EMPTY_HANDLER;
    }
}
