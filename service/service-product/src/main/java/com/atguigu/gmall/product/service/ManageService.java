package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author mqx
 * @date 2020-11-30 10:50:47
 */
public interface ManageService {

    //  先查询所有的一级分类数据
    List<BaseCategory1> findAll();

    //  根据一级分类Id 查询二级分类数据
    List<BaseCategory2> getCategory2(Long category1Id);

    //  根据二级分类Id 查询三级分类数据
    List<BaseCategory3> getCategory3(Long category2Id);

    //  根据分类Id 查询平台属性
    List<BaseAttrInfo> getAttrInfoList(Long category1Id,Long category2Id,Long category3Id);

    /**
     * 大保存 平台属性+平台属性值
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据平台属性Id 获取平台属性值集合
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(Long attrId);


    /**
     * 根据平台属性Id 获取平台属性对象
     * @param attrId
     * @return
     */
    BaseAttrInfo getBaseAttrInfo(Long attrId);

    /**
     *
     * @param spuInfoPage
     * @param spuInfo
     * @return
     */
    IPage getSpuInfoPage(Page<SpuInfo> spuInfoPage,SpuInfo spuInfo);


    /**
     * 获取销售属性集合
     * @return
     */
    List<BaseSaleAttr> getBaseSaleAttrList();

    ////保存spu
    void saveSpuInfo(SpuInfo spuInfo);

    //根据id获取图片列表
    List<SpuImage> getImageList(Long spuId);

    //根据spuId获取销售属性集合
    List<SpuSaleAttr> getSaleAttrList(Long spuId);

    //保存sku
    void saveSkuInfo(SkuInfo skuInfo);

    //查询所有sku数据
    IPage<SkuInfo> getSkuInfoList(Page<SkuInfo> skuInfoPage);

    //商品上架
    void onSale(Long skuId);

    //商品下架
    void cancelSale(Long skuId);

    //根据SkuId获取商品基本信息+图片列表

    SkuInfo getSkuInfoById(Long skuId);

    /**
     * 通过三级分类id查询分类信息
     * select * from base_category_view where id = 61;
     * @param category3Id
     * @return
     */
    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);

    /**
     * 根据skuId 获取最新价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 根据spuId，skuId 查询销售属性集合
     * @param skuId
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);


    Map getSkuValueIdsMap(Long spuId);
}
