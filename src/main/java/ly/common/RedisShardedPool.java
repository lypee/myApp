package ly.common;

import ly.utils.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * redis连接池
 */
public class RedisShardedPool {
    private static ShardedJedisPool pool;//sharded jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-active","20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-idle","20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.min-idle","20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("spring.datasource.druid.test-on-borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("spring.datasource.druid.test-on-return","true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redis1Ip = PropertiesUtil.getProperty("spring.redis1.host");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("spring.redis1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("spring.redis2.host");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("spring.redis2.port"));
    private static String redis3Ip = PropertiesUtil.getProperty("spring.redis3.host");
    private static Integer redis3Port = Integer.parseInt(PropertiesUtil.getProperty("spring.redis3.port") ) ;
    //两个数据库的auth
    private static String redis1Auth = PropertiesUtil.getProperty("spring.redis1.auth");
    private static String redis2Auth = PropertiesUtil.getProperty("spring.redis2.auth");

    //timeout
    private static Integer redisTimeout = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.timeout"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip , redis1Port ,1000 * 2);
             info1.setPassword(redis1Auth);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip ,redis2Port,1000 * 2);
            info2.setPassword(redis2Auth);
        JedisShardInfo info3 = new JedisShardInfo(redis3Ip, redis3Port, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(3);

        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        jedisShardInfoList.add(info3) ;
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static{
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }


    public static void returnBrokenResource(ShardedJedis jedis){
        pool.close();
    }



    public static void returnResource(ShardedJedis jedis){
        pool.close();
    }


    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        ShardedJedisPipeline pipeline = jedis.pipelined() ;
        for(int i = 0 ; i < 100 ; i ++)
        {
           System.out.println(jedis.exists(String.valueOf(i))); ;
        }
        pipeline.sync() ;
        returnResource(jedis);

//        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");


    }
}