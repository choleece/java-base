package cn.choleece.base.framework;

import cn.choleece.base.framework.config.BeanTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-22 22:07
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseFrameworkApplicationTest {
    @Autowired
    private BeanTest beanTest;
}
