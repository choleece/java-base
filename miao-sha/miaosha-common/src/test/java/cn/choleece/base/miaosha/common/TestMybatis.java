package cn.choleece.base.miaosha.common;

import cn.choleece.base.miaosha.common.entity.User;
import cn.choleece.base.miaosha.common.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author choleeceø
 * @Description: TODO
 * @Date 2020-04-25 22:29
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMybatis {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMapper() {
        System.out.println(userMapper.selectById(1));
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("choleece");

        userMapper.insert(user);

        System.out.println(user.toString());

        System.out.println(user.getId());
    }

}
