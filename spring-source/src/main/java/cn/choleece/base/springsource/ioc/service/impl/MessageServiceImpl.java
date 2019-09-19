package cn.choleece.base.springsource.ioc.service.impl;

import cn.choleece.base.springsource.ioc.service.IMessageService;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-19 23:35
 **/
public class MessageServiceImpl implements IMessageService {

    public String getMessage() {
        return "hello world";
    }
}
