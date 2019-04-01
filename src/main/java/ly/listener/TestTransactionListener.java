//package ly.listener;
//
//import org.apache.rocketmq.common.message.Message;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//
//public class TestTransactionListener extends AbstractTransactionListener {
//    private final static Logger log =
//            LoggerFactory.getLogger(TestTransactionListener.class) ;
//    @Override
//    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        log.info(new String(msg.getBody()));
//        return LocalTransactionState.COMMIT_MESSAGE;
//    }
//
//}