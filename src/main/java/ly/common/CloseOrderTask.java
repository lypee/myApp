package ly.common;


import ly.service.UserService;
import ly.utils.PropertiesUtil;
import ly.utils.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
/**
 * 分布式互斥锁
 */
@ComponentScan
public class CloseOrderTask {
    private final static Logger logger = LoggerFactory.getLogger(CloseOrderTask.class) ;
    @Autowired
    private RedissonManage redissonManage ;
    @Autowired
    private UserService userService ;
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del( Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK) ;
    }

    public void closeOrderTask (){
        RLock rLock = redissonManage.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false ;
        try {
            if (getLock = rLock.tryLock(0, 50, TimeUnit.SECONDS)) {
                logger.info("Redisson获取到分布式锁 {}  ,ThreadName: {} ", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getId());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));

            } else {
                logger.info("Redissoon 获取分布式锁 {} 失败 ", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        } catch (Exception e) {
            logger.error("获取分布式锁时发生异常 : {}" , e);
        }finally{
            if(!getLock)
                return  ;
            rLock.unlock();
            logger.info("Redisson分布式锁释放 ") ;

        }
    }
    private void closeOrder(String lockName)
    {
        RedisShardedPoolUtil.expire(lockName , 50 ) ;
        logger.info("获取 :{} ThreadName :{} " , Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK  , Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));

        //iOrderService.closeOrder(hour)
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK) ;
        logger.info("释放 :{} threadName :{} ", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        logger.info("================================");

    }

}