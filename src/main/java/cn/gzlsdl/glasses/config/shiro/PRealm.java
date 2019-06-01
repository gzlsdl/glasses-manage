package cn.gzlsdl.glasses.config.shiro;

import cn.gzlsdl.glasses.modules.dao.UserMapper;
import cn.gzlsdl.glasses.modules.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class PRealm
 * Description: 手机号验证码登录的授权所需数据源
 * @author luxiaobo
 * Created on 2019/6/1
 */
@Component
@Slf4j
public class PRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getName() {
        return "PRealm";
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        User user = (User) pc.getPrimaryPrincipal();
        Long userId = user.getId();
        List<String> permsList = userMapper.queryAllPerms(userId);

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
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

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken ac) throws AuthenticationException {
        String phone = (String) ac.getPrincipal();
        User user = new User();
//        user user=null;
        user.setPhone(phone);
        user = userMapper.selectOne(user);
        if (user == null) {
            throw new UnknownAccountException("该手机号未注册");
        }
        log.info("短信验证码登录");
        return new SimpleAuthenticationInfo(user,"ok",getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token!=null&&(token instanceof PhoneToken);
    }
}
