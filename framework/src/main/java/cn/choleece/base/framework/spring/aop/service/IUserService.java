package cn.choleece.base.framework.spring.aop.service;

import cn.choleece.base.framework.spring.aop.entity.User;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:37
 **/
public interface IUserService {

    User createUser(String firstName, String lastName, int age);

    User queryUser();
}
