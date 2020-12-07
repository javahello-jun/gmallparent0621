package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqx
 * @date 2020-12-4 14:14:12
 */
@Service
public class ItemServiceImpl implements ItemService {


    //  数据汇总
    @Autowired
    private ProductFeignClient productFeignClient;

    //  http://item.gmall.com/39.html;  web-all 39.html  skuId = 39
    @Override
    public Map<String, Object> getBySkuId(Long skuId) {
        Map<String, Object> map = new HashMap<>();
        //  获取数据
        //  select * from sku_info where id =skuId;
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);

        //  获取分类数据
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        //  价格
        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);

        //  获取销售属性销售属性值回显并锁定
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());

        //  获取切换数据
        Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
        //  skuValueIdsMap 转换为Json 字符串
        String mapJson = JSON.toJSONString(skuValueIdsMap);

        // web-all 需要渲染数据，页面需要获取对应的key。key 是谁 从页面找！  Model model model.addAllAttributes()
        //  分类数据的值 =  productFeignClient.get分类数据的方法
        //  map.put("分类数据的key","分类数据的值");
        map.put("categoryView",categoryView);
        map.put("price",skuPrice);
        map.put("valuesSkuJson",mapJson);
        map.put("spuSaleAttrList",spuSaleAttrListCheckBySku);
        map.put("skuInfo",skuInfo);

        return map;
    }
}
