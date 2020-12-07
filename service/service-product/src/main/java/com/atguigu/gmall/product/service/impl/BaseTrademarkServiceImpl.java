package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mqx
 * @date 2020-12-1 14:25:36
 */
//  ServiceImpl 这个类中实现了IService中的方法
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper,BaseTrademark> implements BaseTrademarkService {

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public IPage getBaseTradeMarekList(Page<BaseTrademark> objectPage) {
        //  select * from base_trademark order by id desc;
        QueryWrapper<BaseTrademark> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.orderByDesc("id");
        //  设置一个排序规则！ mysql 默认的排序规则是啥? desc/asc
        return baseTrademarkMapper.selectPage(objectPage,objectQueryWrapper);
    }
}
