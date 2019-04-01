package ly.utils;

import ly.common.Const;
import ly.common.RedisPool;
import ly.common.RedisShardedPool;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry ;
import java.util.concurrent.atomic.AtomicLong;

public class RedisPoolUtil {


    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
//            log.error("expire key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
//            log.error("setex key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
//            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
//            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
//            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 批量删除hash
     * @param bigkey
     */
    public void delBigHash(String bigkey) {
        Jedis jedis = RedisPool.getJedis() ;
        jedis.auth(Const.REDIS_PASSWORD);
        String cursor = "0";
        while (true) {
            ScanResult<Map.Entry<String, String>> scanResult =
                    jedis.hscan(bigkey, cursor , new ScanParams().count(100));
            //获取新游标
            cursor = scanResult.getStringCursor()  ;
            List<Map.Entry<String  ,  String>> list = scanResult.getResult() ;
            if (list == null || list.size() == 0) {
                continue ;
            }
            String [] fields = getFieldsFrom(list) ;
            jedis.hdel(bigkey, fields);
            if ("0".equals(cursor)) {
                break ;
            }
        }
        jedis.del(bigkey);
    }

    /**
     * 获取field数组
     */
    private String[] getFieldsFrom(List<Entry<String  , String >>list){
        List<String> fields = new ArrayList<>();
        for (Entry<String, String> entry : list) {
            fields.add(entry.getKey());
        }
        return fields.toArray(new String[fields.size()]);
    }

//    /**
//     * 统计10w条热点key
//     * @param args
//     */
//    public void getHotKeys()
//    {
//        Jedis jedis = RedisPool.getJedis() ;
//        jedis.auth(Const.REDIS_PASSWORD);
//        List<String> keysList = null  ;
//         jedis.monitor(new JedisMonitor() {
//            @Override
//            public void onCommand(String s) {
//                keysList.add(s) ;
//            }
//        });
//        AtomicLongMap<String> atomicLongMap = AtomicLong
//    }
    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

       for(int i = 0 ; i < 10000000 ; i++)
       {
           RedisShardedPoolUtil.set(String.valueOf(i),"value");
       }

//        String value = RedisShardedPoolUtil.get("keyTest");
//
//        RedisShardedPoolUtil.setEx("keyex","valueex",60*10);
//
//        RedisShardedPoolUtil.expire("keyTest",60*20);
//
//        RedisShardedPoolUtil.del("keyTest");


        String aaa = RedisShardedPoolUtil.get(null);
        System.out.println(aaa);

        System.out.println("end");


    }


}
