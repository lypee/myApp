//package ly.config;
//
//import ly.controller.userController;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.*;
//
///**
// * producer的创建类
// * 一个程序中 只能有一个 此类
// */
//@Configuration
//public class ProducerConfigure {
//    //    public final static Logger logger = LoggerFactory.getLogger(userController.class);
//    public final static Logger logger = LoggerFactory.getLogger(ProducerConfigure.class);
//    @Autowired
//    private ProducerConfig producerConfigure ;
//    /**
//     * 创建消息发送者实例
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "rocketmq.producer" , value = "default" , havingValue = "true")
//    public DefaultMQProducer defaultMQProducer()throws MQClientException{
//        logger.info(producerConfigure.toString());
//        logger.info("defaultProducer 正在创建 ------------------");
//        DefaultMQProducer producer = new DefaultMQProducer(producerConfigure.getGroupName());
//        producer.setNamesrvAddr(producerConfigure.getNamesrvAddr());
//        producer.setVipChannelEnabled(false);
//        producer.setRetryTimesWhenSendAsyncFailed(10);
//        producer.start();
//        logger.info("rockeq producer server 开启成功 ------------------");
//        return producer ;
//    }
//    /**
//     * 创建事务消息发送者实例
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "rocketmq.producer" , value = "transaction" ,havingValue = "true")
//    public TransactionMQProducer transactionMQProducer() throws MQClientException
//    {
//        logger.info(producerConfigure.toString());
//        logger.info("TransactionMQProducer 正在创建--------");
//        TransactionMQProducer producer = new TransactionMQProducer(producerConfigure.getGroupName());
//        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r);
//                thread.setName("client-transaction-msg-check-thrad");
//                return thread ;
//            }
//        });
//        producer.setNamesrvAddr(producerConfigure.getNamesrvAddr());
//        producer.setExecutorService(executorService);
//        producer.start() ;
//        logger.info("TransactionMQProducer server 开启成功 -----");
//        return producer ;
//    }
//    @Bean
//    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "default", havingValue = "true")
//    public DefaultMQProducer defaultProducer() throws MQClientException {
//        logger.info(producerConfigure.toString());
//        logger.info("defaultProducer 正在创建---------------------------------------");
//        DefaultMQProducer producer = new DefaultMQProducer(producerConfigure.getGroupName());
//        producer.setNamesrvAddr(producerConfigure.getNamesrvAddr());
//        producer.setVipChannelEnabled(false);
//        producer.setRetryTimesWhenSendAsyncFailed(10);
//        producer.start();
//        logger.info("rocketmq producer server开启成功---------------------------------.");
//        return producer;
//    }
//}
//
