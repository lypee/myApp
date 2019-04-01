package ly.common;

import ly.utils.PropertiesUtil;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * '互斥锁管理
 */
@Component
public class RedissonManage {
    private Config config = new Config() ;
    private Redisson redisson = null ;
    private final static Logger log = LoggerFactory.getLogger(RedissonManage.class);
    private static String redis1Ip = PropertiesUtil.getProperty("spring.redis1.host");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("spring.redis1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("spring.redis2.host");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("spring.redis2.port"));


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Redisson getRedisson() {
        return redisson;
    }

    public void setRedisson(Redisson redisson) {
        this.redisson = redisson;
    }
   @PostConstruct
    private void init(){
       try {
           config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());

           redisson = (Redisson) Redisson.create(config);

           log.info("初始化Redisson结束");
       } catch (Exception e) {
           log.error("redisson init error",e);
       }
   }
}
