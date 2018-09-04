package com.roncoo.eshop.cache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.spring.SpringContext;
import redis.clients.jedis.JedisCluster;

/**
 * @program: cache
 * @description: ${description}
 * @author: Li YangLin
 * @create: 2018-09-04 22:28
 */
public class SaveShopInfo2ReidsCacheCommand extends HystrixCommand<Boolean> {
    private ShopInfo shopInfo;
    public SaveShopInfo2ReidsCacheCommand(ShopInfo shopInfo){
        super(HystrixCommandGroupKey.Factory.asKey("RedisGroup"));
        this.shopInfo=shopInfo;
    }
    @Override
    protected Boolean run() throws Exception {
        String key = "shop_info_" + shopInfo.getId();
        JedisCluster jedisCluster= (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
        return true;
    }
}
