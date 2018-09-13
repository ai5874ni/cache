package com.roncoo.eshop.cache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.spring.SpringContext;
import redis.clients.jedis.JedisCluster;

/**
 * @program: cache
 * @description: ${description}
 * @author: Li YangLin
 * @create: 2018-09-04 22:28
 */
public class SaveShopInfo2ReidsCacheCommandRedis extends RedisBaseCommand<Boolean> {
    private ShopInfo shopInfo;
    public SaveShopInfo2ReidsCacheCommandRedis(ShopInfo shopInfo){
        this.shopInfo=shopInfo;
    }
    @Override
    protected Boolean run() throws Exception {
        String key = "shop_info_" + shopInfo.getId();
        JedisCluster jedisCluster= (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
        return true;
    }
    @Override
    protected Boolean getFallback() {
        return false;
    }
}
