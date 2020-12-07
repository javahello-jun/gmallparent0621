package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mqx
 * @date 2020-12-1 11:19:20
 */
@RestController
@RequestMapping("admin/product")
@Api(tags = "spuInfo 数据接口")
public class SpuManageController {

    @Autowired
    private ManageService manageService;


    //  springmvc 对象传值：传递过来的参数与实体类的属性名称保持一致，则可以使用实体类接收
    //  http://api.gmall.com/admin/product/{page}/{limit}?category3Id=61
    @GetMapping("{page}/{limit}")
    public Result getSpuList(@PathVariable Long page,
                             @PathVariable Long limit,
//                             HttpServletRequest request,
//                             String category3Id,
                             SpuInfo spuInfo){

//        String category3Id = request.getParameter("category3Id");
        //  处理的分页查询
        //  IPage --> Page
        Page<SpuInfo> spuInfoPage = new Page<>(page,limit);
        //  调用服务层方法
        IPage spuInfoPageList = manageService.getSpuInfoPage(spuInfoPage, spuInfo);
        //  返回数据
        return Result.ok(spuInfoPageList);
    }

    //  http://api.gmall.com/admin/product/baseSaleAttrList
    @GetMapping("baseSaleAttrList")
    public Result baseSaleAttrList(){
        //  返回销售属性集合
        List<BaseSaleAttr> baseSaleAttrList = manageService.getBaseSaleAttrList();
        return Result.ok(baseSaleAttrList);
    }

    //保存spu
    //http://api.gmall.com/admin/product/saveSpuInfo
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    //根据spuId获取图片列表
    //http://api.gmall.com/admin/product/spuImageList/{spuId}
    @GetMapping("spuImageList/{spuId}")
    public Result spuImageList(@PathVariable Long spuId){
        List<SpuImage> imageList = manageService.getImageList(spuId);
        return Result.ok(imageList);
    }

}
