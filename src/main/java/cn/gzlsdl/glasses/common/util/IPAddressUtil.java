package cn.gzlsdl.glasses.common.util;


import javax.servlet.http.HttpServletRequest;


/**
 * Class IPAddressUtil
 * Description:获取客户端真实的ip
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class IPAddressUtil {

    public static String getClientIpAddress(HttpServletRequest request){


        String ip = request.getHeader("X-Forwarded-For");
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}