//package ly.common;
//
//import com.google.common.hash.Funnels;
//import com.google.common.hash.Hashing;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.cache.CacheProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPipeline;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//@Component
//public class BloomFilter {
//    private final static Logger logger = LoggerFactory.getLogger(BloomFilter.class);
//
//    private Jedis jedis ;
//
//    private ShardedJedis shardedJedis ;
//
//    //从连接池中获取连接
//    @PostConstruct
//    public void init(){
//        jedis = RedisPool.getJedis() ;
//        shardedJedis = RedisShardedPool.getJedis()  ;
//    }
//
////    //bit数组长度
////    private long numBits = optimalNumOfBits(Const.BLOOM_FILTER.expectedInsertions,
////            Const.BLOOM_FILTER.FPP);
////    //hash函数数量
////    private int numHashFunctions = optimalNumOfHashFunctions(Const.BLOOM_FILTER.expectedInsertions ,
////            numBits) ;
////
////    //计算hash函数的个数
////    private int optimalNumOfHashFunctions(long n, long m) {
////        return Math.max(1 , (int)Math.round( (double) m/n*Math.log(2) )) ;
////    }
////    //计算bit数组长度
////    private long optimalNumOfBits(long n , double p )
////    {
////        if (p == 0) {
////            p = Double.MIN_VALUE;
////        }
////        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
////    }
////    //判断key是否在集合中
////    public boolean isExits(String where , String key)
////    {
////        long[] indexs = getIndexs(key);
////        boolean result   ;
////        Pipeline pipeline = jedis.pipelined() ;
////        try{
////            for (long index : indexs) {
////                pipeline.getbit(getRedisKey(where), index);
////            }
////            result = !pipeline.syncAndReturnAll().contains(false) ;
////        }finally {
////            try {
////                pipeline.close();
////            } catch (Exception e) {
////                logger.error("BloomFilter Pipe close error! {}" , e);
////                e.printStackTrace();
////            }
////        }
////        if (!result) {
////            put(where, key);
////        }
////        return result ;
////    }
/////**
//// * 将key存入reids bitmap
//// */
////private void put(String where , String key)
////{
////    //写操作 : 写入主redis
////    long[] indexs = getIndexs(key);
////    ShardedJedisPipeline pipeline = shardedJedis.pipelined() ;
////    try{
////        for(long index : indexs )
////        {
////            pipeline.setbit(getRedisKey(where), index, true);
////        }
////        pipeline.sync();
////    }catch (Exception e)
////    {
////        logger.error("BloomFilter put has Error : {} " ,e);
////        e.printStackTrace();
////    }
////
////}
/////**
//// * 根据key获得bitmap下标
//// */
////private long[] getIndexs(String key)
////{
////    long hash1 = hash(key);
////    long hash2 = hash1 >>> 16 ;
////    long[] result = new long[numHashFunctions] ;
////    for (int i = 0; i < numHashFunctions; i++) {
////        long combinedHash = hash1 + i * hash2 ;
////        if (combinedHash < 0) {
////            combinedHash = ~combinedHash ;
////        }
////        result[i]  = combinedHash % numBits ;
////    }
////    return result ;
////}
/////**
//// * 获取一个hash值
//// */
////private long hash(String key)
////{
////    Charset charset = Charset.forName("UTF-8") ;
////    return Hashing.murmur3_128().hashObject(key , Funnels.stringFunnel(charset)).asLong() ;
////}
////private String getRedisKey(String where)
////{
////    return Const.BLOOM_FILTER.REDIS_KEY_PREFIX + where ;
////}
//
//
////bit数组长度
//private long numBits = optimalNumOfBits(Const.BLOOM_FILTER.expectedInsertions, Const.BLOOM_FILTER.FPP);
//    //hash函数数量
//    private int numHashFunctions = optimalNumOfHashFunctions(Const.BLOOM_FILTER.expectedInsertions, numBits);
//
//    //计算hash函数个数 方法来自guava
//    private int optimalNumOfHashFunctions(long n, long m) {
//        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
//    }
//
//    //计算bit数组长度 方法来自guava
//    private long optimalNumOfBits(long n, double p) {
//        if (p == 0) {
//            p = Double.MIN_VALUE;
//        }
//        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
//    }
//
//    /**
//     * 判断keys是否存在于集合where中
//     */
//    public boolean isExist(String where, String key) {
//        long[] indexs = getIndexs(key);
//        boolean result;
//        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
//        Pipeline pipeline = jedis.pipelined();
//        try {
//            for (long index : indexs) {
//                pipeline.getbit(getRedisKey(where), index);
//            }
//            result = !pipeline.syncAndReturnAll().contains(false);
//        } finally {
//            try {
//                pipeline.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (!result) {
//            put(where, key);
//        }
//        return result;
//    }
//
//    /**
//     * 将key存入redis bitmap
//     */
//    private void put(String where, String key) {
//        long[] indexs = getIndexs(key);
//        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
//        Pipeline pipeline = jedis.pipelined();
//        try {
//            for (long index : indexs) {
//                pipeline.setbit(getRedisKey(where), index, true);
//            }
//            pipeline.sync();
//        } finally {
//            try {
//                pipeline.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 根据key获取bitmap下标 方法来自guava
//     */
//    private long[] getIndexs(String key) {
//        long hash1 = hash(key);
//        long hash2 = hash1 >>> 16;
//        long[] result = new long[numHashFunctions];
//        for (int i = 0; i < numHashFunctions; i++) {
//            long combinedHash = hash1 + i * hash2;
//            if (combinedHash < 0) {
//                combinedHash = ~combinedHash;
//            }
//            result[i] = combinedHash % numBits;
//        }
//        return result;
//    }
//
//    /**
//     * 获取一个hash值 方法来自guava
//     */
//    private long hash(String key) {
//        Charset charset = Charset.forName("UTF-8");
//        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(charset)).asLong();
//    }
//
//    private String getRedisKey(String where) {
//        return Const.BLOOM_FILTER.REDIS_KEY_PREFIX + where;
//    }
//    public static void main(String [] args)
//    {
//        Jedis jedis  = RedisPool.getJedis() ;
//        jedis.auth(Const.REDIS_PASSWORD) ;
//        BloomFilter bloomFilter  = new BloomFilter() ;
//        System.out.println( bloomFilter.isExist("ccc", "ccc"));
//        System.out.println( bloomFilter.isExist("ccc", "ccc"));
//        System.out.println(  bloomFilter.isExist("ddd", "ddd"));
//        System.out.println( bloomFilter.isExist("ddd" , "eee"));
//    }
//}
package  ly.common ;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class BloomFilter {
    @Resource
    private JedisPool jedisPool;
    private final static Logger logger  = LoggerFactory.getLogger(BloomFilter.class);
    //预计插入量
    private long expectedInsertions = 1000;
    //可接受的错误率
    private double fpp = 0.001F;
    //布隆过滤器的键在Redis中的前缀 利用它可以统计过滤器对Redis的使用情况
    private String redisKeyPrefix = "user:";
    private static Jedis jedis  = RedisPool.getJedis();
    private static ShardedJedis shardedJedis = RedisShardedPool.getJedis() ;

    //利用该初始化方法从Redis连接池中获取一个Redis链接
//    @PostConstruct
//    public void init(){
//        this.jedis =RedisPool.getJedis();
//    }

    public void setExpectedInsertions(long expectedInsertions) {
        this.expectedInsertions = expectedInsertions;
    }

    public void setFpp(double fpp) {
        this.fpp = fpp;
    }

    public void setRedisKeyPrefix(String redisKeyPrefix) {
        this.redisKeyPrefix = redisKeyPrefix;
    }

    //bit数组长度
    private long numBits = optimalNumOfBits(expectedInsertions, fpp);
    //hash函数数量
    private int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);

    //计算hash函数个数 方法来自guava
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    //计算bit数组长度 方法来自guava
    private long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 判断keys是否存在于集合where中
     */
    public boolean isExist(String where, String key) {
        jedis.auth(Const.REDIS_PASSWORD) ;
        long[] indexs = getIndexs(key);
        boolean result = false ;
        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
//        Pipeline pipeline = jedis.pipelined();
        ShardedJedisPipeline pipeline =  shardedJedis.pipelined() ;
        try {
            for (long index : indexs) {
                pipeline.getbit(getRedisKey(where), index);
            }
//            result = !pipeline.syncAndReturnAll().contains(key);
            List<Object> list = pipeline.syncAndReturnAll() ;
            result = !list.contains(false) ;
        } catch (Exception e)
        {
            logger.error("BloomFilter Query key : {} has error :{} " , key , e);
            e.printStackTrace();
        }
//        finally {
//            try {
//                pipeline.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if (!result) {
            put(where, key);
        }
        return result;
    }

    /**
     * 将key存入redis bitmap
     */
    private void put(String where, String key) {
        long[] indexs = getIndexs(key);
        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
        Pipeline pipeline = jedis.pipelined();
//        ShardedJedisPipeline pipeline = RedisShardedPool.getJedis().pipelined() ;
        try {
            for (long index : indexs) {
                pipeline.setbit(getRedisKey(where), index, true);
            }
            pipeline.sync();
        }
        finally {
            try {
                pipeline.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据key获取bitmap下标 方法来自guava
     */
    private long[] getIndexs(String key) {
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            result[i] = combinedHash % numBits;
        }
        return result;
    }

    /**
     * 获取一个hash值 方法来自guava
     */
    private long hash(String key) {
        Charset charset = Charset.forName("UTF-8");
        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(charset)).asLong();
    }

    private String getRedisKey(String where) {
        return redisKeyPrefix + where;
    }
    public static void main(String [] args)
    {
        BloomFilter bloomFilter  = new BloomFilter() ;
//        System.out.println( bloomFilter.isExist("ccc", "ccc"));
//        System.out.println( bloomFilter.isExist("ccc", "ccc"));
//        System.out.println(  bloomFilter.isExist("ddd", "kkk"));
//        System.out.println( bloomFilter.isExist("ddd" , "eee"));
    }
}