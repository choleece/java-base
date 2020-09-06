package cn.choleece.base.miaosha.common.mapper;

import cn.choleece.base.miaosha.common.entity.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Repository
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 乐观锁更新
     * @param stock stock param
     * @return rows update
     */
    @Update("update stock set version = version + 1, sale = sale + 1, count = count - 1 where id = #{stock.id} and version = #{stock.version}")
    int updateByOptimistic(@Param("stock") Stock stock);

}
