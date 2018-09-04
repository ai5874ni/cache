package com.roncoo.eshop.cache.service.impl;

import javax.annotation.Resource;

import com.roncoo.eshop.cache.hystrix.command.GetProductInfoFromReidsCacheCommand;
import com.roncoo.eshop.cache.hystrix.command.GetShopInfoFromReidsCacheCommand;
import com.roncoo.eshop.cache.hystrix.command.SaveProductInfo2ReidsCacheCommand;
import com.roncoo.eshop.cache.hystrix.command.SaveShopInfo2ReidsCacheCommand;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.service.CacheService;

/**
 * 缓存Service实现类
 *
 * @author Administrator
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    @Resource
    private JedisCluster jedisCluster;

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public ProductInfo getLocalCache(Long id) {
        return null;
    }

    /**
     * 将商品信息保存到本地的ehcache缓存中
     *
     * @param productInfo
     */
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地ehcache缓存中获取商品信息
     *
     * @param productId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     *
     */
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地ehcache缓存中获取店铺信息
     *
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到redis中
     *
     * @param productInfo
     */
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        new SaveProductInfo2ReidsCacheCommand(productInfo).execute();
    }

    /**
     * 将店铺信息保存到redis中
     *
     */
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        new SaveShopInfo2ReidsCacheCommand(shopInfo).execute();
    }

    /**
     * 从redis中获取商品信息
     *
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId) {

        return new GetProductInfoFromReidsCacheCommand(productId).execute();
    }

    /**
     * 从redis中获取店铺信息
     *
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        return new GetShopInfoFromReidsCacheCommand(shopId).execute();
    }

}
