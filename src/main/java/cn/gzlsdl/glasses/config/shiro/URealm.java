package cn.gzlsdl.glasses.config.shiro;


import cn.gzlsdl.glasses.common.util.ShiroUtil;
import cn.gzlsdl.glasses.modules.dao.UserMapper;
import cn.gzlsdl.glasses.modules.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Class ShiroRealm
 * Description:定义账号密码的realm，是shiro保证用户数据安全的数据源配置
 * @author luxiaobo
 * Created on 2019/5/17
 */
@Component
public class URealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userDao;

    @Override
    public String getName() {
        return "URealm";
    }

    /**
     * Method doGetAuthorizationInfo
     *
     *@author luxiaobo
     *@date 2019/5/18
     *
     * 给用户分配授权
     *
     * @param pc (类型：PrincipalCollection)
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        User user= (User) pc.getPrimaryPrincipal();
        Long userId = user.getId();
        List<String> permsList=userDao.queryAllPerms(userId);

        //用户权限列表
        Set<String> permsSet=new HashSet<>();
        for (String perms:permsList){
            if (StringUtils.isBlank(perms)){
                continue;
            }
            /**顶层的是目录，中间的是菜单，最下层是按钮
             * 目录下可能有多个菜单，菜单下可能有多个按钮（新增，修改，删除）
             *多个菜单或按钮用逗号隔开
             * 格式为  sys:user:create,sys:user:update,sys:user:delete
             * 意思表示为 sys代表系统管理目录，user代表用户菜单，另外三个对应新增，修改，删除
             */
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permsSet);
        return info;
    }

    /**
     * Method doGetAuthenticationInfo
     *
     *@author luxiaobo
     *@date 2019/5/18
     *
     * 用户登录认证
     *
     * @param ac (类型：AuthenticationToken)
     * @return AuthenticationInfo
     * @throws AuthenticationException when
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken ac) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) ac;

        //查询当前用户信息
        User user = new User();
        user.setPhone(token.getUsername());
        user=userDao.selectOne(user);

        //该手机号未注册
        if (user==null){
            throw new UnknownAccountException("该手机号未注册！");
        }

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());
        return info;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtil.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
