package cn.choleece.base.miaosha.common.service.impl;

import cn.choleece.base.miaosha.common.entity.User;
import cn.choleece.base.miaosha.common.mapper.UserMapper;
import cn.choleece.base.miaosha.common.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
