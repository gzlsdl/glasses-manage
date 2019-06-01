package cn.gzlsdl.glasses.common.util;


import cn.gzlsdl.glasses.modules.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Class BaseController
 * Description:控制器的基类，通过继承可代码复用，包含日志输出，获取用户，获取用户id，获取部门id
 * @author luxiaobo
 * Created on 2019/5/17
 */
public class BaseController {

    //日志输出
    protected  Logger log= LoggerFactory.getLogger(getClass());

    //获取用户
    protected User getUser(){return (User) SecurityUtils.getSubject().getPrincipal(); }

    //获取用户id
    protected Long getUserId(){
        return getUser().getId();
    }

    //获取用户的部分id
    protected Long getDeptId(){
        return getUser().getDeptId();
    }

    //检查sign签名参数
    protected JSONObject checkParams(String params){
        log.info("传进来的参数是："+params);
        String str=SecurityUtil.base64Decode(params);
        JSONObject jsonObject = JSONObject.parseObject(str);
        String sign = jsonObject.getString("sign");
        String timestamp = jsonObject.getString("timestamp");
        String noncestr = jsonObject.getString("noncestr");

        int signResult = SignSecurityUtil.checkParams(sign, timestamp, noncestr);
        if (0==signResult){
            return jsonObject;
        }else {
            return null;
        }
    }


}
