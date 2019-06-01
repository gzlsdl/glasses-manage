package cn.gzlsdl.glasses.common.util;


import cn.gzlsdl.glasses.common.constant.Constant;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;


/**
 * Class MiaoDiSend
 * Description:调用秒滴科技发送短信
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class MiaoDiSend extends BaseController{

    private static Logger log= LoggerFactory.getLogger(MiaoDiSend.class);


    /**
     * Method excute
     *
     *@author luxiaobo
     *@date 2019/5/17
     *
     *
     * @param phone (类型：String)
     * @param tplId (类型：String)
     * @param params (类型：String)
     */
    public static int excute(String phone,String tplId,String params){
        try {
            StringBuilder sb=new StringBuilder();
            sb.append("accountSid=").append(Constant.MDSMS_ACCOUNT_SID);
            sb.append("&to=").append(phone);
            sb.append("&param=").append(URLEncoder.encode(params,"UTF-8"));
            sb.append("&templateid=").append(tplId);
            String body=sb.toString()+HttpUtil.createCommonParam(Constant.MDSMS_ACCOUNT_SID,Constant.MDSMS_AUTH_TOKEN);
            String result=HttpUtil.post(Constant.MDSMS_REST_URL,body);
            JSONObject json = JSONObject.parseObject(result);
            System.out.println(result);
            if ("00000".equals(json.getString("respCode"))){
                log.info("短信验证码："+params+",发送成功！");
                return 0;
            }else {
                log.error("短信验证码未正常发送！");
                return -6;
            }
        }catch (Exception e){
            log.error("短信验证码发送失败!");
            return -6;
        }
    }


    public static void main(String[] args) {
//        13424107219
        MiaoDiSend send = new MiaoDiSend();
        send.excute("18318592826",Constant.TEMPLATEID,RandomVerCodeUtil.getVerCode());
    }

}
