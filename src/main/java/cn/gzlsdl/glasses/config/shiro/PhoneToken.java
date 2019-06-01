package cn.gzlsdl.glasses.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;


/**
 * Class PhoneToken
 * Description:自定义手机短信验证码登录的验证令牌
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Data
public class PhoneToken implements AuthenticationToken {


    //手机号码
    private String phone;
    //登录类型，构造器初始化时，固定传入“pRealm”
    private String loginType;

    @Override
    public Object getPrincipal() {
            return getPhone();
    }

    //因为是手机号登录，验证码校验通过就可以了
    //这个地方原本返回getPassword（），这个默认返回ok
    //在PRealm中的认证器默认写死ok了
    @Override
    public Object getCredentials() {
            return "ok";
    }

    public PhoneToken() {
    }

    public PhoneToken(String phone) {
        this.phone = phone;
        this.loginType="PRealm";
    }

}
