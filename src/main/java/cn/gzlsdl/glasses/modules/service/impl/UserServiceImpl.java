package cn.gzlsdl.glasses.modules.service.impl;

import cn.gzlsdl.glasses.common.constant.Constant;
import cn.gzlsdl.glasses.common.exception.GlassesException;
import cn.gzlsdl.glasses.common.util.MiaoDiSend;
import cn.gzlsdl.glasses.common.util.PageUtil;
import cn.gzlsdl.glasses.common.util.Query;
import cn.gzlsdl.glasses.common.util.RandomVerCodeUtil;
import cn.gzlsdl.glasses.config.redis.RedisCacheClient;
import cn.gzlsdl.glasses.modules.dao.InviteCodeMapper;
import cn.gzlsdl.glasses.modules.dao.UserMapper;
import cn.gzlsdl.glasses.modules.entity.Dept;
import cn.gzlsdl.glasses.modules.entity.InviteCode;
import cn.gzlsdl.glasses.modules.entity.User;
import cn.gzlsdl.glasses.modules.service.DeptService;
import cn.gzlsdl.glasses.modules.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userDao;

    @Autowired
    private InviteCodeMapper inviteCodeDao;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RedisCacheClient redis;


    /**
     * Method isPhoneExist
     *
     * @param phone (类型：String)
     * @return boolean
     * @author luxiaobo
     * @date 2019/5/18
     * <p>
     * 查看手机号是否已注册
     */
    @Override
    public boolean isPhoneExist(String phone) {
        User user = new User();
        user.setPhone(phone);
        user = userDao.selectOne(user);

        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method checkInviteCode
     *
     * @param inviteCode (类型：String)
     * @return int
     * @author luxiaobo
     * @date 2019/5/18
     * <p>
     * 检查邀请码是否正确
     * 如果数据库中没有此邀请码，返回2
     * 如果有，但是已经被注册过，返回1
     * 正确 返回0
     */
    @Override
    public int checkInviteCode(String inviteCode) {
        InviteCode code = new InviteCode();
        code.setCode(inviteCode);
        code = inviteCodeDao.selectOne(code);

        if (code == null) {
            return 5;
        } else {
            if (code.getStatus() == 1) {
                return 6;
            } else {
                return 0;
            }
        }
    }

    @Override
    public int sendVerCode(String phone, String clientIpAddress,String type) throws GlassesException {
        String verCode = RandomVerCodeUtil.getVerCode();
        //先把验证码存到redis，检查是否有恶意请求，再决定发送验证码
        int result = redis.cacheForVerCodeOfIpMaxCount( phone, verCode,type, 60, clientIpAddress);
        if (result == 1) {
            log.info("当前验证码未过期，请稍后再试");
            return result;
        } else if (result == 2) {
            log.info("当前手机号超过今日验证码发送次数");
            return result;
        } else if (result == 3) {
            log.info("当前ip超过今日验证码发送次数");
            return result;
        }else if (result==-5){
            log.info("redis出错");
            return result;
        }

        log.info("验证码：" + verCode + "已发送至号码为：" + phone + "的手机上");
        int miaoDiResult=MiaoDiSend.excute(phone, Constant.TEMPLATEID, verCode);
        return miaoDiResult;
    }

    @Override
    public int register(String phone, String verCodeFromClient, String inviteCode) throws GlassesException {
        String verCodeFromRedis = null;         //从redis取来的验证码
        try {
            verCodeFromRedis = redis.getCacheValue(Constant.REG_VERCODE_PREFIX + phone);
        } catch (Exception e) {
            log.error("从redis取验证码失败");
            return -5;
        }
        if (verCodeFromRedis == null || verCodeFromRedis.length() == 0) {
            return 1;
        }
        User user = null;
        if (verCodeFromClient.equals(verCodeFromRedis)) {
            //用户注册，将数据插入user表
            try {
                user = new User();
                user.setPhone(phone);
                user.setCreateTime(new Date());
                user.setStatus(1);
                userDao.insert(user);
                log.info("手机号为：" + phone + "的用户注册成功");
            } catch (Exception e) {
                log.error("用户注册失败");
                return -4;
            }
            try {
                //该用户注册的邀请码设为失效，并将该邀请码的使用者存储为该用户
                inviteCodeDao.updateStatusByInviteCode(1, inviteCode);
                return 0;
            } catch (Exception e) {
                log.error("使邀请码失效失败", e);
                return -4;
            }

        } else {
            log.error("验证码不正确");
            return 2;
        }
    }

    @Override
    public int updateByPhone(String phone, String company, String city, String province, String size) {
        try {
            EntityWrapper<User> wrapper = new EntityWrapper<>();
            wrapper.where("phone={0}",phone);
            User user=new User();
            user.setCity(city);
            user.setProvince(province);
            if (size!=null){
                user.setSize(size);
            }
            user.setCompany(company);
            userDao.update(user,wrapper);
            return 0;
        }catch (Exception e){
            log.error("完善用户信息sql出错");
            return -4;
        }
    }

    @Override
    public PageUtil queryPage(Map<String, Object> map) {
        String phone= (String) map.get("phone");
        Page<User> page=this.selectPage(
                new Query<User>(map).getPage(),
                new EntityWrapper<User>()
                .like(StringUtils.isNotBlank(phone),"phone",phone)
                .addFilterIfNeed(map.get(Constant.SQL_FILTER)!=null, (String) map.get(Constant.SQL_FILTER))
        );

        for (User user:page.getRecords()){
            Dept dept=deptService.selectById(user.getDeptId());
            user.setDeptName(dept.getDeptName());
        }

        return new PageUtil(page);
    }

    @Override
    public void update(User user) {

    }


}
