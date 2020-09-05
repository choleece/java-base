package cn.choleece.base.springboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author choleece
 * @Description: the util of log
 * @Date 2020-09-05 22:35
 **/
public class LogUtils {

    static public final Logger consoleLog = LoggerFactory.getLogger("cn.choleece.base.log.console");

    static public final Logger infoLog = LoggerFactory.getLogger("cn.choleece.base.log.info");

    static public final Logger warnLog = LoggerFactory.getLogger("cn.choleece.base.log.warn");

    static public final Logger errorLog = LoggerFactory.getLogger("cn.choleece.base.log.error");

    public static Logger clazzLog(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
