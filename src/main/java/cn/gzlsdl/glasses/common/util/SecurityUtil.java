package cn.gzlsdl.glasses.common.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * Class SecurityUtil
 * Description:加密工具类 base64、MD5、sha256
 *
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class SecurityUtil {

    public static String base64Encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

    public static String base64Decode(String data) {
        return new String(Base64.decodeBase64(data.getBytes()), StandardCharsets.UTF_8);
    }

    public static String md5Hex(String data) {
        return DigestUtils.md5Hex(data.getBytes());
    }

    public static String sha256Hex(String data) {
        return DigestUtils.sha256Hex(data.getBytes());
    }

    public static void main(String[] args) throws IOException {
        String n = "294831";
        Long t1 = System.currentTimeMillis();
        String t = t1.toString();
        String sign = "noncestr=" + n + "&timestamp=" + t + "&token=GZLSDL&version=V1.0";
        System.out.println(md5Hex(sign).toUpperCase());
        System.out.println(t);

        String data = "{\"timestamp\":\"1559123280063\",\"noncestr\":\"294831\",\"sign\":\"EB0CE2C862608DCD5C361C9063FA3F4B\",\"phone\":\"18307681258\",\"loginVerCode\":\"666666\"}";
        System.out.println(base64Encode(data));


    }
}
