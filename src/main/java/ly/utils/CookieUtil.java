package ly.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
   static  Logger logger = LoggerFactory.getLogger(CookieUtil.class);
    private final static String COOKIE_DOMAIN = ".lypng.com";
//private final static String COOKIE_DOMAIN = ".3rhvam.natappfree.cc";
    private final static String COOKIE_NAME = "login_token";

    public static String readLoginToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies() ;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info("read cookieName :{} , cookieValue:{} ",
                        cookie.getName(), cookie.getValue());
                if(StringUtils.equals(cookie.getName() , COOKIE_NAME)){
                    logger.info("return  cookieName:{} , cookieValue:{} " ,
                            cookie.getName() , cookie.getValue());
                    return cookie.getValue() ;
                }
            }
        }
        return null ;
    }

    public static void writeLoginToken(HttpServletResponse httpServletResponse,
                                       String token) {
        //指定cookie的name 和 value
        Cookie cookie = new Cookie(COOKIE_NAME, token);

        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        //设置js脚本无法读取cookie 防止xss
        cookie.setHttpOnly(true);
        //设置cookie写入硬盘
        cookie.setMaxAge(60 * 60 * 24 * 365);

        logger.info("write cookieName:{} , cookieValue:{} success ", cookie.getName(), cookie.getValue());
        httpServletResponse.addCookie(cookie);
    }

    public static void delLoginToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies() ;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    //代表删除cookie
                    cookie.setMaxAge(0);
                    logger.info("del cookieName:{} cookieValue:{} success ",
                            cookie.getName(), cookie.getValue());
                    httpServletResponse.addCookie(cookie);
                    return ;
                }
            }
        }
    }
}
