package cn.choleece.base.miaosha.common.util;

import java.io.Serializable;

/**
 * @author choleece
 * @Description: 统一返回
 * @Date 2020-04-25 23:26
 **/
public class R  implements Serializable {

    private static final long serialVersionUID = -6287952131441663819L;

    private int code;

    private String msg;

    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 服务器返回成功码
     */
    public static final int SUCCESS_CODE = 0;

    /**
     * 服务器返回失败码
     */
    private static final int ERROR_CODE = -1;

    /**
     * 操作信息
     */
    private static final String SUCCESS_MSG = "操作成功";

    public static R error(int code, String msg) {
        R result = new R();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static R error(String msg) {
        return error(ERROR_CODE, msg);
    }

    public static R error() {
        return error("服务器开小差了...");
    }

    /**
     * 返回msg & data
     * @param msg
     * @param object
     * @return {"code": 0, "msg": msg, "data": object}
     */
    public static R ok(String msg, Object object) {
        R result = new R();
        result.setCode(SUCCESS_CODE);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    /**
     * 返回带data的内容
     * @param object
     * @return {"code": 0, "data": null}
     */
    public static R ok(Object object) {
        return ok(SUCCESS_MSG, object);
    }

    /**
     * 返回不带msg的结果
     * @return {"code": 0}
     */
    public static R ok() {
        R result = new R();
        result.setCode(SUCCESS_CODE);
        return result;
    }
}
