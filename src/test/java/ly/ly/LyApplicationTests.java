package ly.ly;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan(basePackages = {"ly.config" , "ly.listener", "ly.common"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class LyApplicationTests {

    @Test
    public void contextLoads() {
    }

}
