//package ly.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//
///**
// * 消息队列Config
// * 读取yml文件配置的Util类
// */
//@Component
//@PropertySource("classpath:application.properties")
//@ConfigurationProperties(prefix = "rocketmq.producer")
//@Configuration
//public class ProducerConfig {
//  private String namesrvAddr ;
//  private String groupName ;
//
//    @Override
//    public String toString() {
//        return "ProducerConfig{" +
//                "namesrvAddr='" + namesrvAddr + '\'' +
//                ", groupName='" + groupName + '\'' +
//                '}';
//    }
//
//    public String getNamesrvAddr() {
//        return namesrvAddr;
//    }
//
//    public void setNamesrvAddr(String namesrvAddr) {
//        this.namesrvAddr = namesrvAddr;
//    }
//
//    public String getGroupName() {
//        return groupName;
//    }
//
//    public void setGroupName(String groupName) {
//        this.groupName = groupName;
//    }
//}
