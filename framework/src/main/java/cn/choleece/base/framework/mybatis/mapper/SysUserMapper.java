package cn.choleece.base.framework.mybatis.mapper;

import cn.choleece.base.framework.mybatis.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-20 22:36
 **/
@Repository
public interface SysUserMapper {

    List<SysUser> listUsers();
}
