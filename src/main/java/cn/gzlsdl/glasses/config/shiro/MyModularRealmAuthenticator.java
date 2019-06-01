package cn.gzlsdl.glasses.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.HashMap;

/**
 * Class MyModularRealmAuthenticator
 * Description:shiro的认证策略 这里是覆盖了它的默认策略，多realm认证
 * @author luxiaobo
 * Created on 2019/6/1
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken at) throws AuthenticationException {
        assertRealmsConfigured();
        PhoneToken token= (PhoneToken) at;
        String loginType=token.getLoginType();
        Collection<Realm> realms=getRealms();
        HashMap<String,Realm> realmType=new HashMap<>(realms.size());
        for (Realm realm:realms){
            realmType.put(realm.getName(),realm);
        }

        if (realmType.get(loginType)!=null){
            return doSingleRealmAuthentication(realmType.get(loginType),token);
        }else {
            return doMultiRealmAuthentication(realms,token);
        }


    }
}
