package ly.common;

import ly.utils.JsonUtil;
import ly.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

@Component
public class RedisPool  {
    private static JedisPool pool;//jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-active","20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-idle","20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.min-idle","20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("spring.datasource.druid.test-on-borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("spring.datasource.druid.test-on-return","true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redisIp = PropertiesUtil.getProperty("spring.redis1.host");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("spring.redis1.port"));
    private static Integer redisTimeOut = Integer.parseInt(PropertiesUtil.getProperty("spring.redis.timeout"));
 //初始化连接池
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        pool = new JedisPool(config , redisIp , redisPort , redisTimeOut);
    }

    static{
        initPool();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }


    public static void returnBrokenResource(Jedis jedis){
        pool.close();
    }



    public static void returnResource(Jedis jedis){
        pool.close();
    }


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(RedisPool.class);

        Jedis jedis = pool.getResource();
        jedis.auth(Const.REDIS_PASSWORD) ;
        jedis.set(Const.USER_REDIS_SESSION + ":" +  1 , "2" , "nx" , "ex" , Const.RedisCacheExtime.REDIS_CACHE_EXTIME);


        returnResource(jedis);
//

    }







}