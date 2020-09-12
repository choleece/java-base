package cn.choleece.base.springboot.mybatis.mapper;

import cn.choleece.base.springboot.mybatis.entity.SysUser;
import cn.choleece.base.springboot.mybatis.injector.CustomMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author choleece
 * @Description: 系统用户mapper
 * @Date 2020-09-12 21:18
 **/
@Repository
public interface SysUserMapper extends CustomMapper<SysUser> {


    /**
     * list sys users
     * @param page page
     * @return page of sys users
     */
    IPage<SysUser> listSysUsers(@Param("page") Page page);

    /**
     * 利用wrapper当条件查询
     * @param wrapper sql wrapper
     * @return list of sys user
     * 这里的wrapper参数必须用$，不能使用#
     */
    @Select("select * from sys_user ${ew.customSqlSegment}")
    List<SysUser> listSysUserByWrapper(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);
}
