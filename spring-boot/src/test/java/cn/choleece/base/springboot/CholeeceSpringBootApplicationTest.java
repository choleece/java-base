package cn.choleece.base.springboot;

import cn.choleece.base.springboot.mybatis.entity.SysUser;
import cn.choleece.base.springboot.mybatis.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: application test
 * @Date 2019-09-22 22:07
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class CholeeceSpringBootApplicationTest {
    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    public void testInsertSysUser() {
        System.out.println(sysUserMapper.insert(new SysUser().setName("choleece").setPhone("18202802615")));
    }

    @Test
    public void testListSysUsers() {
        System.out.println(sysUserMapper.listSysUsers(new Page(1, 10)));
    }

    @Test
    public void testListSysUsersByWrapper() {
        System.out.println(sysUserMapper.listSysUserByWrapper(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getName, "choleece")));
    }
}
