package ly.utils;

import com.alibaba.druid.util.StringUtils;
import ly.pojo.User;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference ;
import java.text.SimpleDateFormat;

/**
 * 序列化
 */
public class JsonUtil {
    private static ObjectMapper objectMapper =
            new ObjectMapper() ;

    static{
        //将对象所有字段列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认转化成timestamp
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //日期统一
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略 在json字符串中存在但是在java对象中不存在对应属性的情况
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String obj2Strong(T obj) {
        if (obj == null) {
            return null ;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }
    public static<T> String obj2StringPretty(T obj){
        if(obj == null){
            return null ;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
      public static <T> String obj2String(T obj) {
          if (obj == null) {
              return null;
          }
          try {
              return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
        /**
         * 集合类的转换
         */
        public static <T> T string2Obj(String str , TypeReference<T> typeReference)
        {
            if (StringUtils.isEmpty(str) || typeReference == null) {
                return null ;
            }
            try {
                return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
            } catch (Exception e) {
                e.printStackTrace();
                return null ;
            }
        }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass , elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }
    public static void main(String[]args)
    {
//        User u1 = new User.userBuilder().buildEmail("237454712@qq.com").buildName("ly").build();
//        u1.setGender("1");
//        System.out.println(u1.getGender());
//        u1.setUserId(444);
//        u1.setUserName("lyly");
//        String u1JsonStr = JsonUtil.obj2String(u1);
//        System.out.println(u1JsonStr);
//        User u2 = JsonUtil.string2Obj(u1JsonStr, User.class);
//        System.out.println(u2.getUserName() + " " + u2.getUserId());
    }
    }
