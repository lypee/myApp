//package ly.listener;
//
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public abstract class AbstractTransactionListener implements TransactionListener {
//
//    @Override
//    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//        return LocalTransactionState.COMMIT_MESSAGE;
//    }
//
//}