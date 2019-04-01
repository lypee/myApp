package ly.controller;


import com.alibaba.fastjson.JSONObject;
import ly.common.*;
import ly.pojo.User;
import ly.pojo.vo.userVo;
import ly.service.UserService;
import ly.utils.HttpRequest;
import ly.utils.JsonUtil;
import ly.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController  extends BasicController{
    public final static Logger logger = LoggerFactory.getLogger(userController.class);
    @Autowired
    private UserService userService ;
    @Autowired
    private static  BloomFilter bloomFilter = new BloomFilter() ;
    
    private String appId;
    private String secret;
    private String APIUrl;

    @RequestMapping(value = "/hello" )
    public String  Hello(){

        return "niaho"  ;
    }

    @PostMapping("/test")
    @ResponseBody
    public ServerResponse test(@RequestBody JSONObject Code  , HttpSession httpSession , HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse) {
       String code = Code.getString("code");
        Map<String, Object> map = openId(code) ;
        String openid =(String) map.get("openId") ;
        System.out.println(bloomFilter.isExist(Const.BLOOM_FILTER.REDIS_KEY_PREFIX , openid));
        return ServerResponse.createBySuccess(openid) ;
    }
//    @PostMapping("/register")
//    @ResponseBody
//    public ServerResponse registerUser(@RequestBody  User user) {
//        if(!userService.getUserIdByUserName(user.getUserName()).isSuccess())
//        {
//            return ServerResponse.createByErrorMessage("用户已存在") ;
//        }
//        //将userId设置成uuid的hex值
//        user.setUserId(Math.abs(UUID.randomUUID().hashCode()));
//        if (userService.insertUser(user) != 0) {
//            return ServerResponse.createBySuccess("用户注册成功");
//        }else {
//            return ServerResponse.createByErrorMessage("用户注册失败");
//        }
//    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    @ResponseBody
    public ServerResponse login(@RequestBody  userVo userVo, HttpSession httpSession , HttpServletResponse httpServletResponse , HttpServletRequest httpServletRequest) throws  Exception{
        String code = userVo.getCode() ;
        String userName = userVo.getUserName() ;
        String gender = userVo.getGender()  ;
        Map<String, Object> map = openId(code) ;
        String userOpenId = (String)map.get("openId") ;
        Integer userId = userService.findUserIdByUserOpenId(userOpenId) ;
        //新建 返回session中的user
        User user = new User.userBuilder().buildGender(gender).buildName(userName).buildUserOpenId(userOpenId).buildUserId(userId).build() ;
        //bloomfilter 判断用户是否合法
        if(bloomFilter.isExist(Const.BLOOM_FILTER.REDIS_KEY_PREFIX , userOpenId)){

        }
        //异步方法 :
        ServerResponse response = userService.login(userOpenId , userName) ;
        if(response.getData() == null ) return ServerResponse.createByErrorMessage("操作错误");
        logger.info("user :{} login " ,userName);
        //登陆成功 写入redis
        Jedis jedis = RedisPool.getJedis() ;
        jedis.auth(Const.REDIS_PASSWORD);
        if(userService.login(userOpenId , userName).isSuccess() ){
            //表明用户已经登陆过
            jedis.set(Const.USER_REDIS_SESSION + ":" +  userId ,JsonUtil.obj2String(user) , "nx" , "ex" , Const.RedisCacheExtime.REDIS_CACHE_EXTIME);
            // System.out.println(httpSession.getId()+" ,"+Const.RedisCacheExtime.REDIS_CACHE_EXTIME+ " , " + JsonUtil.obj2String(user));
            //  将user放入session中
            httpSession.setAttribute(Const.CURRENT_USER, user);
            System.out.println("成功");
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("出现错误~") ;
    }
//    /**
//     * 修改密码 不需要确认登陆
//     *  *** 未使用RequestBody接口
//     * @return
//     */
//    @PostMapping("/changePass")
//    @ResponseBody
//    public ServerResponse changePass( User user, String newPassword , HttpServletRequest httpServletRequest)
//    {
//        if(StringUtils.isEmpty(newPassword))
//        //检查新旧密码是否正确
//        if(userService.checkUserLegel(user.getUserName() ,MD5Util.encode(user.getUserPassword())) == -1 )
//        {
//            return ServerResponse.createByErrorMessage("当前密码错误") ;
//        }
//        //密码加密 get
//        user.setUserPassword(MD5Util.encode(newPassword) );
//        if(userService.changePassword(user) != 0)
//        {
//            return ServerResponse.createBySuccess("修改密码成功");
//        }else {
//            return ServerResponse.createByErrorMessage("用户不存在或修改失败");
//        }
//    }

    /**
     * 确认用户是登陆状态
     * check redis中有这个
     */
    @PostMapping("/checkIsLogin")
    @ResponseBody
    public ServerResponse checkIsLogin(User user){
        String userOpenId = user.getUserOpenId() ;
        if(StringUtils.isEmpty(userOpenId)) {
            return ServerResponse.createByError() ;
        }
        //从 从redis中获得用户登陆信息
        if (shardedJedis.exists(Const.USER_REDIS_SESSION + ":" + user.getUserId())) {
            return ServerResponse.createBySuccess() ;
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEET_LOGIN.getCode() ,"不是登陆状态 ") ;
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    @CacheEvict(value = "accountCache" , allEntries = true)
    @ResponseBody
    public ServerResponse logout(HttpServletRequest httpServletRequest ,HttpServletResponse httpServletResponse ,
                                 HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        Jedis jedis = RedisPool.getJedis() ;
        jedis.auth(Const.REDIS_PASSWORD);
        user.setUserId(userService.findUserIdByUserOpenId(user.getUserOpenId()));
        String userToken = (String)httpSession.getAttribute(Const.USER_TOKEN);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest) ;

        //返回1 正常退出 ,
        if(jedis.del(Const.USER_REDIS_SESSION + ":" + userToken) == 1 ) {

//            CookieUtil.delLoginToken(httpServletRequest , httpServletResponse);
            httpSession.removeAttribute(Const.CURRENT_USER);
            return ServerResponse.createBySuccess("退出成功");
        }
        //其它返回值 : maybe ttl 归零 .
        //无论是否正常退出 ,都移除session中的attribute
        httpSession.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createByErrorMessage("非法退出") ;
    }
//    /**
//     * 获取用户权限
//     */
//    @PostMapping("/getUserLevelByUserName")
//    public ServerResponse getUserLevelByUserName(User user)
//    {
//        Integer role = userService.getUserLevelByUserName(user).getData() ;
//        if(userService.getUserLevelByUserName(user).isSuccess()){
//            return ServerResponse.createBySuccess(role) ;
//        }
//        return ServerResponse.createByErrorMessage("用户不存在");
//    }
//    /**
//     * 解密用户敏感数据
//     *
//     * @param encryptedData 明文,加密数据
//     * @param iv            加密算法的初始向量
//     * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/decodeUserInfo", method = RequestMethod.POST)
//    public Map decodeUserInfo(String encryptedData, String iv, String code) {
//
//        Map map = new HashMap();
//
//        //登录凭证不能为空
//        if (code == null || code.length() == 0) {
//            map.put("status", 0);
//            map.put("msg", "code 不能为空");
//            return map;
//        }
//
//        //小程序唯一标识   (在微信小程序管理后台获取)
//        String wxspAppid = "xxxxxxxxxxxxxx";
//        //小程序的 app secret (在微信小程序管理后台获取)
//        String wxspSecret = "xxxxxxxxxxxxxx";
//        //授权（必填）
//        String grant_type = "authorization_code";
//
//
//        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
//        //请求参数
//        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
//        //发送请求
//        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
//        //解析相应内容（转换成json对象）
//        JSONObject json = JSONObject.fromObject(sr);
//        //获取会话密钥（session_key）
//        String session_key = json.get("session_key").toString();
//        //用户的唯一标识（openid）
//        String openid = (String) json.get("openid");
//
//        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
//        try {
//            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
//            if (null != result && result.length() > 0) {
//                map.put("status", 1);
//                map.put("msg", "解密成功");
//
//                JSONObject userInfoJSON = JSONObject.fromObject(result);
//                Map userInfo = new HashMap();
//                userInfo.put("openId", userInfoJSON.get("openId"));
//                userInfo.put("nickName", userInfoJSON.get("nickName"));
//                userInfo.put("gender", userInfoJSON.get("gender"));
//                userInfo.put("city", userInfoJSON.get("city"));
//                userInfo.put("province", userInfoJSON.get("province"));
//                userInfo.put("country", userInfoJSON.get("country"));
//                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
//                userInfo.put("unionId", userInfoJSON.get("unionId"));
//                map.put("userInfo", userInfo);
//                return map;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        map.put("status", 0);
//        map.put("msg", "解密失败");
//        return map;
//    }


    /**
     * 获得用户对应的openid
     * @param code 每次登陆时的code
     * @return 包含状态码和信息的HashMap
     */
    @RequestMapping("/openId")
    @ResponseBody
    public Map<String, Object> openId(String code){ // 小程序端获取的CODE

        appId = PropertiesUtil.getProperty("app.appId") ;
        secret = PropertiesUtil.getProperty("app.secret");
        APIUrl  = PropertiesUtil.getProperty("app.APIUrl") ;
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        try {
            boolean check = (StringUtils.isEmpty(code)) ? true : false;
            if (check) {
                throw new Exception("参数异常");
            }
            StringBuilder urlPath = new StringBuilder(APIUrl); // 微信提供的API，这里最好也放在配置文件
            urlPath.append(String.format("?appid=%s", appId));
            urlPath.append(String.format("&secret=%s", secret));
            urlPath.append(String.format("&js_code=%s", code));
            urlPath.append(String.format("&grant_type=%s", "authorization_code")); // 固定值
            String data = HttpRequest.sendGet(urlPath.toString(),  ""); // java的网络请求，这里是我自己封装的一个工具包，返回字符串
//            System.out.println("请求结果：" + data);
//            String openId = new JSONObject(data).getString("openid");
            //获取openId

//            String openId = new JSONObject()..getString("openid") ;
            String openId = JSONObject.parseObject(data).getString("openid");
            System.out.println("获得openIdddddddd: " + openId);
            result.put("openId", openId);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("remark", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
