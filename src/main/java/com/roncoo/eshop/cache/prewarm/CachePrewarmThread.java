package com.roncoo.eshop.cache.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.service.CacheService;
import com.roncoo.eshop.cache.spring.SpringContext;
import com.roncoo.eshop.cache.zk.ZooKeeperSession;

/**
 * @program: cache
 * @description: ${description}
 * @author: Li YangLin
 * @create: 2018-08-25 22:05
 */
public class CachePrewarmThread extends Thread {
    @Override
    public void run() {
        CacheService cacheService= (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        ZooKeeperSession zookeeperSession = ZooKeeperSession.getInstance();
        String taskidlist = zookeeperSession.getNodeData("/taskid-list");
        if (taskidlist != null && !"".equals(taskidlist)) {
            String[] taskidListSplited = taskidlist.split(",");
            for (String taskid : taskidListSplited) {
                String taskidPath = "/taskid-lock-" + taskid;
                boolean result = zookeeperSession.acquireFastFailedDistributedLock(taskidPath);
                if (!result) {
                    continue;
                }
                String taskidStatusLockPath = "/taskid-status-lock-" + taskid;
                zookeeperSession.acquireDistributedLock(taskidStatusLockPath);
                String taskidStatus = zookeeperSession.getNodeData("/taskid-status-" + taskid);
                System.out.println("【CachePrewarmThread获取task的预热状态】taskid=" + taskid + ", status=" + taskidStatus);
                if ("".equals(taskidStatus)) {
                    String productidList = zookeeperSession.getNodeData("/task-hot-product-list-" + taskid);
                    JSONArray productIdJOSNArray = JSONArray.parseArray(productidList);
                    for (int i = 0; i < productIdJOSNArray.size(); i++) {
                        Long productId = productIdJOSNArray.getLong(i);
                        String productInfoJSON = "{\"id\":" + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        System.out.println("【CachePrwarmThread将商品数据设置到本地缓存中】productInfo=" + productInfo);
                        cacheService.saveProductInfo2ReidsCache(productInfo);
                        System.out.println("【CachePrwarmThread将商品数据设置到redis缓存中】productInfo=" + productInfo);
                    }
                    zookeeperSession.setNodeData("/taskid-status-" + taskid,"success");
                }
                zookeeperSession.releaseDistributedLock(taskidStatusLockPath);
                zookeeperSession.releaseDistributedLock(taskidPath);
            }
        }
    }
}
