package ly.common;

/**
 * 常量类
 */
public class Const {
    public static final String AES_KEY = "Lpyng.com@!#@lpy";
    public static final String CURRENT_USER = "currentUser" ;
    public static final String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";
    public static final String REDIS_PASSWORD="ly123456";
    public static final String USER_REDIS_SESSION="user-redis-session" ;
    public static final String USER_TOKEN="userToken";


    /**
     * 表示 操作权限
     * 防止横向越权
     */
    public interface Role{
        //普通注册用户
        int ROLE_CUSTOMER = 0 ;
        //管理员用户
        int ROLE_ADMIN = 1 ;
    }
    /**
     * 对应计划的完成状况
     */
    public interface  Status{
        //表示 已经 完成过
        // 不限次数
        int FINISHED = 1 ;
        // 表示 对应的计划 并没有 完成
        int UN_FINISHED = 0 ;
    }
    /**
     * 表示计划的类型
     * 常用 仅用一次
     *
     */
    public interface Type{
        int ONLY_ONCE = 0  ;
        int UN_CERTAIN = 1 ;
    }
    /**
     * redis的操作
     */
    public interface RedisCacheExtime{
        int REDIS_CACHE_EXTIME= 1800;
        int REDIS_NULL_EXTIME = 60 ;
    }
    public interface REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK =  "CLOSE_ORDER_TASK_LOCK" ;
    }
    public interface BLOOM_FILTER{
        //预计插入量
        long expectedInsertions = 1000  ;
        //接收容错率
        double FPP = 0.001F ;
        //布隆过滤器在Redis中的前缀
        String REDIS_KEY_PREFIX = "bf:" ;
    }
}
