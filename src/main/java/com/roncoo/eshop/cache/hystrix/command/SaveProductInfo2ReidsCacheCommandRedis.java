package com.roncoo.eshop.cache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.spring.SpringContext;
import redis.clients.jedis.JedisCluster;

/**
 * @program: cache
 * @description: ${description}
 * @author: Li YangLin
 * @create: 2018-09-04 22:19
 */
public class SaveProductInfo2ReidsCacheCommandRedis extends RedisBaseCommand<Boolean> {
    private ProductInfo productInfo;
    public SaveProductInfo2ReidsCacheCommandRedis(ProductInfo productInfo) {
        this.productInfo=productInfo;
    }

    @Override
    protected Boolean run() throws Exception {
        String key = "product_info_" + productInfo.getId();
        JedisCluster jedisCluster= (JedisCluster) SpringContext.getApplicationContext().getBean("JedisClusterFactory");
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
        return true;
    }

    @Override
    protected Boolean getFallback() {
        return false;
    }
}
