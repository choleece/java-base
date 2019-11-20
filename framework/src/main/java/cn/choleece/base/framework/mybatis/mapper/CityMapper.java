package cn.choleece.base.framework.mybatis.mapper;

import cn.choleece.base.framework.mybatis.entity.City;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-20 22:36
 **/
@Repository
public interface CityMapper {

    @Select("select * from city")
    List<City> listCities();
}
