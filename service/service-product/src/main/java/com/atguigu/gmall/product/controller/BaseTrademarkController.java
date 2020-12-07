package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mqx
 * @date 2020-12-1 11:55:27
 */
@Api(tags = "品牌数据接口")
@RestController // @ResponseBody + @Controller
@RequestMapping("admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    //  http://api.gmall.com/admin/product/baseTrademark/{page}/{limit}
    @GetMapping("{page}/{limit}")
    public Result getBaseTradeMarkList(@PathVariable Long page,
                                       @PathVariable Long limit){

        Page<BaseTrademark> objectPage = new Page<>(page, limit);
        IPage baseTradeMarekList =  baseTrademarkService.getBaseTradeMarekList(objectPage);
        return Result.ok(baseTradeMarekList);
    }
    //  http://api.gmall.com/admin/product/baseTrademark/save
    //  接收前台传递过来的json 字符串 ，使用@RequestBody 转换为java 对象
    //  @RequestBody  @ResponseBody
    @PostMapping("save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        //  此时不需要这么做了！ 独自写一个BaseTrademarkService
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    //  http://api.gmall.com/admin/product/baseTrademark/update
    @PutMapping("update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    //  http://api.gmall.com/admin/product/baseTrademark/remove/{id}
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    //  http://api.gmall.com/admin/product/baseTrademark/get/{id}
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    //  http://api.gmall.com/admin/product/baseTrademark/getTrademarkList
    @GetMapping("getTrademarkList")
    public Result getTrademarkList(){
        //  select * from base_trademark;
        return Result.ok(baseTrademarkService.list(null));
    }

}
