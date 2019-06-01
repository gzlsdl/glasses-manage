package cn.gzlsdl.glasses.modules.service;

import cn.gzlsdl.glasses.common.exception.GlassesException;
import cn.gzlsdl.glasses.common.util.PageUtil;
import cn.gzlsdl.glasses.modules.entity.User;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
public interface UserService extends IService<User> {

    boolean isPhoneExist(String phone);

    int checkInviteCode(String inviteCode);

    int sendVerCode(String phone, String clientIpAddress,String type) throws GlassesException;

    int register(String phone, String verCodeFromClient,String inviteCode) throws GlassesException;

    int updateByPhone(String phone, String company, String city, String province, String size);

    PageUtil queryPage(Map<String, Object> map);

    void update(User user);
}
