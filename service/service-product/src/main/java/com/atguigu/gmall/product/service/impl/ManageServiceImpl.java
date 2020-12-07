package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqx
 * @date 2020-11-30 10:54:39
 */
@Service
public class ManageServiceImpl implements ManageService {

    //  注入mapper
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private  SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Override
    public List<BaseCategory1> findAll() {

        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        //  select * from base_category2 where category1_id = category1Id
        return baseCategory2Mapper.selectList(new QueryWrapper<BaseCategory2>().eq("category1_id",category1Id));
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        //  select * from base_category3 where category2_id = category2Id;

        return baseCategory3Mapper.selectList(new QueryWrapper<BaseCategory3>().eq("category2_id",category2Id));
    }

    /**
     * 根据分类Id 查询平台属性
     * 后续会有一个业务，即需要查询 平台属性， 也需要查询平台属性值
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        /*
        单表查询：select * from base_attr_info where category_id = 61 and category_level = 3;
        demo:
        select *
        from base_attr_info bai inner join  base_attr_value bav
        on bai.id = bav.attr_id
        where bai.category_id = 61 and  bai.category_level =3;
         */
        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id,category2Id,category3Id);
    }

    //  baseAttrInfo ,baseAttrValue;
    //  在这个实现类中，既有保存，又有修改！
    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //  baseAttrInfo
        if (baseAttrInfo.getId()==null){
            // 保存
            baseAttrInfoMapper.insert(baseAttrInfo);
        }else {
            //  修改
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }

        //  baseAttrValue
        //  先删除，再插入数据。
        //  delete from base_attr_value where attr_id = baseAttrInfo.getId();
        //  弊端： 删除完成之后，原始的Id不存在！
        QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper = new QueryWrapper<>();
        baseAttrValueQueryWrapper.eq("attr_id",baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueQueryWrapper);

        //  如下操作 保存
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (!CollectionUtils.isEmpty(attrValueList)){
            //  循环遍历   ,iter : 增强for
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //  给attrId 赋值。 baseAttrInfo.id = baseAttrValue.attrId
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        //  select * from base_attr_value where attr_id = attrId;
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",attrId));
    }

    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        //  select * from base_attr_info where id = attrId;
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo!=null){
            //  获取平台属性值集合，将属性值集合放入该对象
            //  select * from base_attr_value where attr_id = attrId;
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }

        return baseAttrInfo;
    }

    @Override
    public IPage getSpuInfoPage(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo) {

        //  构建查询条件
        //  http://api.gmall.com/admin/product/{page}/{limit}?category3Id=61
        //  select * from spu_info where category3_id = ? order by id desc;
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("category3_id",spuInfo.getCategory3Id());
        //  设置一个排序规ba则！ mysql 默认的排序规则是啥? desc/asc
        spuInfoQueryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(spuInfoPage,spuInfoQueryWrapper);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        spuInfoMapper.insert(spuInfo);
        //商品的图片集合
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(!CollectionUtils.isEmpty(spuImageList)){
            for (SpuImage spuImage : spuImageList) {
                Long id = spuInfo.getId();
                spuImage.setSpuId(id);
                spuImageMapper.insert(spuImage);
            }
        }
        // 销售属性集合
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();

        if(!CollectionUtils.isEmpty(spuSaleAttrList)){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                Long id = spuInfo.getId();
                spuSaleAttr.setSpuId(id);
                spuSaleAttrMapper.insert(spuSaleAttr);
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
               if(!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                   for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                       Long id1 = spuInfo.getId();
                       spuSaleAttrValue.setSpuId(id1);
                       String saleAttrName = spuSaleAttr.getSaleAttrName();
                       spuSaleAttrValue.setSaleAttrName(saleAttrName);
                       spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                   }
               }
            }
        }

    }
    //根据id获取图片列表
    @Override
    public List<SpuImage> getImageList(Long spuId) {
        QueryWrapper<SpuImage> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("spu_Id",spuId);
        return spuImageMapper.selectList(spuInfoQueryWrapper);
    }


    //根据spuId获取销售属性集
    @Override
    public List<SpuSaleAttr> getSaleAttrList(Long spuId) {

        return spuImageMapper.selectSpuSaleAttrList(spuId);
    }

    //保存sku
    @Override
    @Transactional
    public void saveSkuInfo(SkuInfo skuInfo) {

        skuInfoMapper.insert(skuInfo);
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(!CollectionUtils.isEmpty(skuImageList)){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }


//        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
//        if(!CollectionUtils.isEmpty(skuSaleAttrValueList)){
//            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
//                skuSaleAttrValue.setSkuId(skuInfo.getId());
//                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
//                skuAttrValueMapper.insert(skuSaleAttrValue);
//
//            }
//        }
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                //  需要补充数据
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                //  添加数据
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }

    }

    @Override
    public IPage<SkuInfo> getSkuInfoList(Page<SkuInfo> skuInfoPage) {

        QueryWrapper skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.orderByDesc("id");
        return skuInfoMapper.selectPage(skuInfoPage,skuInfoQueryWrapper);
    }

    //商品上架
    @Override
    public void onSale(Long skuId) {
       SkuInfo skuInfo = new SkuInfo();
       skuInfo.setIsSale(1);
       skuInfo.setId(skuId);
       skuInfoMapper.updateById(skuInfo);
    }

    //商品下架
    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(0);
        skuInfo.setId(skuId);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("sku_id",skuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        skuInfo.setSkuImageList(skuImageList);
        //  返回数据
        return skuInfo;
    }

    @Override
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo!=null){
            return skuInfo.getPrice();
        }else {
            return new BigDecimal(0);
        }
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId,spuId);
    }

    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        //  存储数据 {"99|100":"38","99|101":"39"} map.put("99|100","38");
        //  通过 mapper 查询数据，并将其放入map 集合  分析使用哪个mapper？
        //  第一种解决方案：自定义一个 Ssav DTO; skuId ,valueIds; List<Ssav>
        //  第二种解决方案：map 来代替  map.put("skuId","38") ;  ssav.setSkuId("38")
        //  map 中的key 就是实体类的属性！
        /*
            class Ssav{
                private Long skuId;
                private String valueIds;
            }

            a.字段相对较少，
            b.不是频繁被使用的时候。
         */
        //   List<Ssav> mapList = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
        List<Map> mapList = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
        if (!CollectionUtils.isEmpty(mapList)){
            //  循环遍历
            for (Map map : mapList) {
                hashMap.put(map.get("value_ids"),map.get("sku_id"));
            }
        }
        //  返回数据
        return hashMap;
    }
}
