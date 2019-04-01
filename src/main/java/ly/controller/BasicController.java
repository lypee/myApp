package ly.controller;

import ly.common.RedisPool;
import ly.common.RedisShardedPool;
import ly.common.SerializeDeserializeWrapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * 遵循重用对象 的编程思想
 */
public class BasicController {
    //写操作的jedis 需要设置auth
    public final static Jedis jedis = RedisPool.getJedis() ;

    //读操作的jedis 不需要
    public  final static ShardedJedis shardedJedis = RedisShardedPool.getJedis() ;

    //预定义序列化类
    SerializeDeserializeWrapper serializeWrapper = null ;

    //反序列化对象
    SerializeDeserializeWrapper deserializeWrapper = null ;
}
