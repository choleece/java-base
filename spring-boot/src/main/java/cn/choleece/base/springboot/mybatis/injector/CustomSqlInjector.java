package cn.choleece.base.springboot.mybatis.injector;

import cn.choleece.base.springboot.mybatis.injector.method.DeleteAllMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author choleece
 * @Description: 自定义sql injector
 * @Date 2020-09-13 00:33
 **/
@Component
public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new DeleteAllMethod());

        // mp选装器, 这里可以进行批量插入，但是要注意的点是，字段为空也会进行插入
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}
