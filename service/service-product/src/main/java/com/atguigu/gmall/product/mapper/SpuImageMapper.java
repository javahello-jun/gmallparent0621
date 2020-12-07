package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpuImageMapper extends BaseMapper<SpuImage> {
/*
*
select * from spu_sale_attr ssa inner join spu_sale_attr_value  ssav
on ssa.spu_id=ssav.spu_id and ssa.base_sale_attr_id=ssav.base_sale_attr_id
where ssa.spu_id=22
* */

    List<SpuSaleAttr> selectSpuSaleAttrList(Long spuId);
}
