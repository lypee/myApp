package ly;

import ly.common.BloomFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"ly.config" , "ly.listener", "ly.common"})
@SpringBootApplication
@EnableCaching //开启缓存
public class LyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyApplication.class, args);
    }

}
