package cn.choleece.base.framework.spring.aop.service.impl;

import cn.choleece.base.framework.spring.aop.entity.User;
import cn.choleece.base.framework.spring.aop.service.IUserService;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:40
 **/
public class UserServiceImpl implements IUserService {

    @Override
    public User createUser(String firstName, String lastName, int age) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        return user;
    }

    @Override
    public User queryUser() {
        User user = new User();
        user.setFirstName("test");
        user.setLastName("test");
        user.setAge(20);
        return user;
    }
}
