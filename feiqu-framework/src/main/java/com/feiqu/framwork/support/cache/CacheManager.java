package com.feiqu.framwork.support.cache;

import com.alibaba.fastjson.JSON;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.JedisUtil;
import com.feiqu.framwork.util.RedisStaticUtil;
import com.feiqu.framwork.util.RedisUtil;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.FqUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */

public class CacheManager {

    private static Logger logger = LoggerFactory.getLogger(CacheManager.class);

    /*public static //缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
            LoadingCache<Integer, com.feiqu.system.pojo.cache.FqUserCache> userCache
            //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
            =  CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                //refreshAfterWrite(3, TimeUnit.HOURS)// 给定时间内没有被读/写访问，则回收。
                .concurrencyLevel(8)
                .expireAfterAccess(60, TimeUnit.SECONDS)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        logger.info(notification.getKey() + " was removed, cause is " + notification.getCause());
                    }
                })
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                //*/

    /**
     * 当本地缓存命没有中时，调用load方法获取结果并将结果缓存
     **//*　　
                .build(
                        new CacheLoader<Integer, com.cwd.boring.pojo.cache.FqUserCache>() {
                            public com.cwd.boring.pojo.cache.FqUserCache load(Integer key) throws Exception {
                                logger.info("load user " + key);
                                FqUserService fqUserService = (FqUserService) SpringContextUtil.getBean("fqUserServiceImpl");
                                FqUser fqUser = fqUserService.selectByPrimaryKey(key);
                                return new FqUserCache(fqUser);
                            }
                        }
                );*/
    public static FqUserCache getUserCacheByUid(Integer userId) {
//        try {


        /*FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
        FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
        FqUserCache fqUserCache = new FqUserCache(fqUser);
        return fqUserCache;*/
        FqUserCache fqUserCache = null;
        try {
            String cacheKey = "FqUser.id:" + userId;
            String userCacheStr = (String) RedisStaticUtil.get(cacheKey);
            if (StringUtils.isEmpty(userCacheStr)) {
                FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
                FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
                if (fqUser == null) {
                    return null;
                }
                fqUserCache = new FqUserCache(fqUser);
                RedisStaticUtil.set(cacheKey, JSON.toJSONString(fqUserCache));
               RedisStaticUtil.expire(cacheKey, 300);
                return fqUserCache;
            } else {
                fqUserCache = JSON.parseObject(userCacheStr, FqUserCache.class);
                return fqUserCache;
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            // 
        }
        return fqUserCache;


        /*Level1CacheSupport level1CacheSupport = Level1CacheSupport.getInstance();

        FqUserCache level1FqUserCache = level1CacheSupport.get(cacheKey);
        if(level1FqUserCache == null){
            RedisObject redisUser = new RedisObject(cacheKey);
            fqUserCache = redisUser.get();
            if(fqUserCache == null){
                FqUserService fqUserService = (FqUserService) SpringContextUtil.getBean("fqUserServiceImpl");
                FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
                fqUserCache = new FqUserCache(fqUser);
                redisUser.set(fqUserCache,300);
                level1CacheSupport.set(cacheKey,fqUserCache);
//                logger.info("一二缓存未查到，去数据库查到 user {}" , userId);
                return fqUserCache;
            }else {
//                logger.info("本地缓存未查到，redis查到 user {}" , userId);
                level1CacheSupport.set(cacheKey,fqUserCache);
                return fqUserCache;
            }
        }else {
//            logger.info("本地缓存查到 user {}" , userId);
            return level1FqUserCache;
        }*/


//            return userCache.get(userId);
//        } catch (ExecutionException e) {
//            logger.error("getUserCacheByUid :"+userId, e);
//        }
//        return null;
    }

    public static void refreshUserCacheByUid(Integer userId) {
        String cacheKey = "FqUser.id:" + userId;
        try {
            FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            FqUserCache fqUserCache = new FqUserCache(fqUser);
         RedisStaticUtil.set(cacheKey, JSON.toJSONString(fqUserCache));
            RedisStaticUtil.expire(cacheKey, 300);
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid", e);
        } finally {
        }
    }

    public static void refreshUserCacheByUser(FqUserCache user) {
        refreshUserCacheByUid(user.getId());
    }

    public static boolean isCollect(String type, Integer uid, Integer thoughtId) {
        try {
            String key = getCollectKey(type, uid);
            return JedisUtil.me().sismember(key, thoughtId.toString());
        } finally {
        }
    }

    public static void addCollect(String type, Integer uid, Integer thoughtId) {
        try {
            String key = getCollectKey(type, uid);
            RedisStaticUtil.set(key, thoughtId.toString());
           RedisStaticUtil.expire(key, 24 * 60 * 60);
        } finally {
        }
    }

    public static void removeCollect(String type, Integer uid, Integer thoughtId) {
        try {
            String key = getCollectKey(type, uid);
           JedisUtil.me().srem(key, thoughtId.toString());
        } finally {
        }
    }

    public static void refreshCollect(String type, Integer uid, List<Integer> list) {
        try {
            String key = getCollectKey(type, uid);
            for (Integer tid : list) {
                RedisStaticUtil.set(key, tid.toString());
            }
            RedisStaticUtil.expire(key, 24 * 60 * 60);
        } finally {
        }
    }

    public static String getCollectKey(String type, Integer uid) {
        return type.concat(":").concat(uid.toString()).concat(":collected");
    }

    public static String getUserBackImgKey(Integer uid) {
        return "fq:bgImg:" + uid;
    }


    public static void refreshWebsiteCache() {
        try {
          RedisStaticUtil.del(CommonConstant.FQ_WEBSITE_ALL);
        } finally {
        }
    }
}
