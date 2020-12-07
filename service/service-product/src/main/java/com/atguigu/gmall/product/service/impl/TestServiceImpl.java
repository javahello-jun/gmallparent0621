package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public synchronized void testLock() {
        //String数据类型 放入数据的方法 jedis jedis.set(key,value);
        String values = redisTemplate.opsForValue().get("num");
        if(StringUtils.isEmpty(values)){
            return;
           }
        int num = Integer.parseInt(values);
        redisTemplate.opsForValue().set("num",String.valueOf(++num));

    }
}
