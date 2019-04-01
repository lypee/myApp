package ly.utils;

import java.security.MessageDigest;

/**
 * util 密码加密
 * salt : set .
 */
public class MD5Util {
    //    private static LoggerFactory loggerFactory = LoggerFactory.getLogger(PropertiesUtil.class);
    /*
    private static String byteArrayToHexString(byte[] b)
    {
        StringBuffer stringBuffer = new StringBuffer() ;
        for (int i = 0; i < b.length; i++) {
            stringBuffer.append(b[i]);
        }
        return stringBuffer.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b ;
        if (n < 0) {
            n += 256  ;
        }
        int d1 = n / 16 ;
        int d2 = n % 16 ;
        return hexDigits[d1] + hexDigits[d2] ;
    }

    /**
     * 返回大写的MD5
     */
//    private static String MD5Encode(String origin, String charsetname) {
//        String ansString = null ;
//        try {
//            ansString = new String(origin);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            if (charsetname == null || "".equals(charsetname)) {
//                ansString = byteArrayToHexString(md.digest(ansString.getBytes())) ;
//            } else {
//                ansString = byteArrayToHexString(md.digest(ansString.getBytes(charsetname)));
//            }
//        } catch (Exception e) {
//            System.out.println("发生异常");
//        }
//        return ansString.toUpperCase() ;
//    }
//
//    public static String MD5EncodeUtf8(String string) {
//        string = string + PropertiesUtil.getProperty("password.salt","lllpy");
//        return MD5Encode(string , "utf-8");
//    }
//    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
//            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static  String encode(String password) {
//        password = password + PropertiesUtil.getProperty("password.salt") ;
        password = password + "lllllpy719" ;
        MessageDigest md5 = null ;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }
        char[] charArray = password.toCharArray() ;
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (byte md5Byte : md5Bytes) {
            int val = ( (int) md5Byte ) & 0xff ;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString() ;
    }

    public static void main(String[]args)
    {
        String temp =  "1" ;
        System.out.println(MD5Util.encode(temp));
    }
}
