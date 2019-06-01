package cn.gzlsdl.glasses.common.util;

import org.apache.commons.lang.StringUtils;


/**
 * Class SignSecurityUtil
 * Description:sign签名的安全校验工具类
 * @author luxiaobo
 * Created on 2019/6/1
 */
public class SignSecurityUtil {


    public static int checkParams(String sign,String timeStamp,String noncestr){
        int param=isAllNotEmpty(sign,timeStamp,noncestr);
        if (param==0){
            int isReal=sign(sign,timeStamp,noncestr);
            if (0==isReal){
                return 0;
            }else if (-2==isReal){
                return -2;
            }else {
                return -3;
            }
        }else {
            return param;
        }
    }

    public static int sign(String sign,String timeStampClient,String noncestr){
        long startTime=Long.parseLong(timeStampClient);
        long endTime=System.currentTimeMillis();
        if (((endTime-startTime)/(1000*60))<=15){
            String encodeServer=SecurityUtil.md5Hex(sort(noncestr,timeStampClient)).toUpperCase();
            if (encodeServer.equals(sign)){
                return 0;
            }else {
                return -2;
            }
        }else {
            return -3;
        }
    }

    public static int isAllNotEmpty(String sign,String timeStamp,String noncestr){
        if (StringUtils.isNotEmpty(sign)&&StringUtils.isNotEmpty(timeStamp)&&StringUtils.isNotEmpty(noncestr)){
            return 0;
        }else {
            return -1;
        }
    }

    public static String sort(String n,String t){
        return "noncestr="+n+"&timestamp="+t+"&token=GZLSDL&version=V1.0";
    }

}
