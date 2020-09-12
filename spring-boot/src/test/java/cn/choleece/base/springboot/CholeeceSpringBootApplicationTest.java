package cn.choleece.base.springboot;

import cn.choleece.base.springboot.mybatis.config.MybatisPlusConfig;
import cn.choleece.base.springboot.mybatis.entity.SysUser;
import cn.choleece.base.springboot.mybatis.mapper.SysUserMapper;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

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

    @Test
    public void testInsertAutoFill() {
        System.out.println(sysUserMapper.insert(new SysUser().setName("choleece").setPhone("15764236208")));
    }

    @Test
    public void testIfAutoFillWithNotEmptyFiled() {
        System.out.println(sysUserMapper.insert(new SysUser().setName("choleece").setPhone("15764236208").setCreateName("chaoli")));
    }

    @Test
    public void testUpdateAutoFill() {
        SysUser user = new SysUser().setName("zheng");
        user.setId(1304803718273851394L);
        System.out.println(sysUserMapper.updateById(user));
    }

    @Test
    public void testUpdateAutoFillWithNotEmptyField() {
        SysUser user = new SysUser().setName("choleece").setUpdateTime(DateUtil.now());
        user.setId(1304803718273851394L);
        System.out.println(sysUserMapper.updateById(user));
    }

    @Test
    public void testDynamicTables() {
        MybatisPlusConfig.tableNames.set("sys_user_01");

        System.out.println(sysUserMapper.listSysUsers(new Page(1, 10)));
    }

    @Test
    public void testCustomInjector() {
        System.out.println(sysUserMapper.deleteAll());
    }

    @Test
    public void testBatchInsert() {
        System.out.println(sysUserMapper.insertBatchSomeColumn(Arrays.asList(new SysUser().setName("choleece"),
                new SysUser().setName("chaoli"))));
    }
}
