package cn.choleece.base.framework.spring.factorybean;

/**
 * @author choleece
 * @Description: UserServiceImpl
 * @Date 2020-10-29 21:53
 **/
public class UserServiceImpl implements IUserService {
    @Override
    public void printName() {
        System.out.println("my name is : choleece");
    }
}
