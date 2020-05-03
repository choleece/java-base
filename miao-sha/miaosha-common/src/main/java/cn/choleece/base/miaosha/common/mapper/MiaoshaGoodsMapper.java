package cn.choleece.base.miaosha.common.mapper;

import cn.choleece.base.miaosha.common.entity.MiaoshaGoods;
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
public interface MiaoshaGoodsMapper extends BaseMapper<MiaoshaGoods> {

    /**
     * 利用数据库乐观锁
     * @param goods
     * @return
     */
    @Update("update miaosha_goods set stock_count = #{goods.stockCount} where id = #{goods.id} and stock_count - #{goods.stockCount} > 0")
    int updateMiaoshaGoods(@Param("goods") MiaoshaGoods goods);


}
