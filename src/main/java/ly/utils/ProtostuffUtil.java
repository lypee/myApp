package ly.utils;


import ly.common.SerializeDeserializeWrapper ;
import avro.shaded.com.google.common.collect.Maps;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import ly.common.Const;
import ly.common.RedisPool;
import ly.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * protostuff序列化工具类
 */
public class ProtostuffUtil {
    private final static Logger logger =
            LoggerFactory.getLogger(ProtostuffUtil.class);
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<>() ;
    //序列化/反序列化包装类对象
    private static final Class<SerializeDeserializeWrapper>WRAPPER_CLASS =
            SerializeDeserializeWrapper.class ;
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA =
            RuntimeSchema.createFrom(WRAPPER_CLASS);
    //缓存对象及对象的schema集合
    private static final Map<Class<?> , Schema<?>> CACHE_SCHEMA = Maps.newConcurrentMap() ;
    static {
        //无法直接序列化的对象
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);
        WRAPPER_SET.add(Vector.class);

        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(SortedMap.class);
        WRAPPER_SET.add(Map.class);

        WRAPPER_SET.add(Object.class);
    }
    //需要包装的类型class对象
    public static void registerWrapperClass(Class clazz)
    {
        WRAPPER_SET.add(clazz);
    }

    @SuppressWarnings({"unchecked" , "rawtypes"})
    private static <T> Schema <T> getSchema(Class<T> cls)
    {
        Schema<T> schema = (Schema<T>) CACHE_SCHEMA.get(cls);
        if(schema == null)
        {
            schema = RuntimeSchema.createFrom(cls);
            CACHE_SCHEMA.put(cls, schema);
        }
        return schema ;
    }


    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass() ;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Object serializeObject = obj;
            Schema schema = WRAPPER_SCHEMA;
            if (!WRAPPER_SET.contains(clazz)) {
                schema = getSchema(clazz);
            } else {
                serializeObject = SerializeDeserializeWrapper.builder(obj);
            }
            return ProtostuffIOUtil.toByteArray(serializeObject, schema, buffer);
        } catch (Exception e) {
            logger.error("序列化对象异常 [ " + obj + " ] " ,e );
            throw new IllegalStateException(e.getMessage(), e);
        }finally {
            buffer.clear();
        }
    }
    /**
     * 反序列化对象
     */
    public static<T> T deserialize(byte[] data , Class<T> clazz)
    {
        try {
            if (!WRAPPER_SET.contains(clazz)) {
                T message = clazz.newInstance();
                Schema<T> schema = getSchema(clazz);
                ProtostuffIOUtil.mergeFrom(data, message, schema);
                return message;
            } else {
                SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
                ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
                return wrapper.getData();
            }
        } catch (Exception e) {
            logger.error("反序列化对象 :{} 异常  :{}", clazz.getName(), e);
            throw new IllegalStateException(e.getMessage() , e) ;
        }
    }
    public static void main(String[]args)
    {
        Jedis jedis = RedisPool.getJedis() ;
        jedis.auth(Const.REDIS_PASSWORD) ;
        User user1 = new User() ;

        user1.setUserId(123);

        User user2 = new User() ;

        user2.setUserId(456) ;
        User user3 = new User() ;
        user3.setUserName("31241");
        List<User> userList = new ArrayList<>(3) ;
        userList.add(user1);
        userList.add(user2) ;
        userList.add(user3) ;


        SerializeDeserializeWrapper wrapper = SerializeDeserializeWrapper.builder(user1);
        byte[] string = ProtostuffUtil.serialize(wrapper) ;

        String stringgg = new String(string) ;
        jedis.set("test" , stringgg ) ;
        byte[] stringggg = jedis.get("test").getBytes() ;


        SerializeDeserializeWrapper deserializeWrapper = ProtostuffUtil.deserialize(stringggg, SerializeDeserializeWrapper.class);
        User getUser = (User)(deserializeWrapper.getData())  ;
//        List<User> getUserList =(List<User>)deserializeWrapper.getData() ;

                System.out.println("system is end");

    }
}
