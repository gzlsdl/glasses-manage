package cn.gzlsdl.glasses.modules.controller;

import cn.gzlsdl.glasses.common.constant.Constant;
import cn.gzlsdl.glasses.common.exception.GlassesException;
import cn.gzlsdl.glasses.common.result.R;
import cn.gzlsdl.glasses.common.util.*;
import cn.gzlsdl.glasses.config.redis.RedisCacheClient;
import cn.gzlsdl.glasses.config.shiro.PhoneToken;
import cn.gzlsdl.glasses.modules.dao.InviteCodeMapper;
import cn.gzlsdl.glasses.modules.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private InviteCodeMapper inviteCodeDao;

    @Autowired
    private RedisCacheClient redis;


    /**
     * Method register
     *
     * @param request (类型：HttpServletRequest)
     * @return R
     * @throws GlassesException when
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 发送验证码
     */
    @RequestMapping(value = "/sendVerCode", method = RequestMethod.POST)
    public R sendVerCode(@RequestBody String params, HttpServletRequest request) throws GlassesException {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");
        String inviteCode = jsonObject.getString("inviteCode");

        //检查data有没有空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(inviteCode)) {
            return R.error(-1, "手机号或邀请码为空");
        }

        //查看手机号是否被注册
        if (userService.isPhoneExist(phone)) {
            return R.error(303, "该手机号已被注册");
        }

        //查看邀请码是否正确
        int status = userService.checkInviteCode(inviteCode);
        if (status == 5) {
            return R.error(399, "该邀请码无效");
        } else if (status == 6) {
            return R.error(399, "该邀请码已失效");
        } else {
            //发送验证码
            int result = userService.sendVerCode(phone, IPAddressUtil.getClientIpAddress(request), Constant.REG_VERCODE_PREFIX);
            inviteCodeDao.updateRegisterUser(inviteCode, phone);
            if (0 == result) {
                return R.ok(0, "邀请码正确,验证码已发送");
            } else if (1 == result) {
                return R.error(399, "当前验证码未过期，不必发送");
            } else if (2 == result) {
                return R.error(399, "当前手机号超过今日验证码发送次数");
            } else if (3 == result) {
                return R.error(399, "当前ip超过今日验证码发送次数");
            } else if (-5 == result) {
                return R.error(-5, "redis出错");
            } else {
                return R.error(-6, "短信验证码出错");
            }
        }
    }


    /**
     * Method register
     *
     * @param params (类型：Map<String, Object>)
     * @return R
     * @throws GlassesException when
     * @author luxiaobo
     * @date 2019/5/20
     * <p>
     * 用户注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public R register(@RequestBody String params) throws GlassesException {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");
        String inviteCode = jsonObject.getString("inviteCode");
        String verCodeFromClient = jsonObject.getString("verCode");

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(inviteCode) || StringUtils.isEmpty(verCodeFromClient)) {
            return R.error(-1, "手机号或邀请码或验证码为空");
        }

        //查看手机号是否被注册
        if (userService.isPhoneExist(phone)) {
            return R.error(303, "该手机号已被注册");
        } else if (!phone.equals(inviteCodeDao.selectByCode(inviteCode))) {
            return R.error(399, "请用相匹配的手机号注册");
        }

        //查看邀请码是否正确 0正常，1邀请码失效，2邀请码无效
        int status = userService.checkInviteCode(inviteCode);
        if (status == 2) {
            return R.error(301, "该邀请码无效");
        } else if (status == 1) {
            return R.error(302, "该邀请码已失效");
        } else {
            int result = userService.register(phone, verCodeFromClient, inviteCode);
            if (result == 0) {
                return R.ok(0, "注册成功");
            } else if (result == 2) {
                return R.error(304, "验证码不正确");
            } else if (result == 1) {
                return R.error(305, "验证码已失效");
            } else if (result == -5) {
                return R.error(-5, "redis出错");
            } else {
                return R.error(-4, "mysql出错");
            }
        }
    }


    /**
     * Method perfectMsg
     *
     * @param params (类型：String)
     * @return R
     * @author luxiaobo
     * @date 2019/5/27
     * <p>
     * 完善注册信息
     */
    @RequestMapping(value = "/perfectMsg", method = RequestMethod.POST)
    public R perfectMsg(@RequestBody String params) {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        String company = jsonObject.getString("company");
        String size = jsonObject.getString("size");

        int result = userService.updateByPhone(phone, company, city, province, size);
        if (0 == result) {
            log.info("信息已完善");
            try {
                Subject subject = ShiroUtil.getSubject();
                PhoneToken token = new PhoneToken(phone);
                subject.login(token);
            }catch (UnknownAccountException e) {
                return R.error(399, "账号不存在");
            } catch (IncorrectCredentialsException e) {
                return R.error(399, "账号密码不正确");
            } catch (AuthenticationException e) {
                log.error(e.getMessage());
                return R.error(399, "认证失败");
            }
            return R.ok("信息已完善,准备进入主页");
        } else {
            log.error("完善信息sql出错");
            return R.error(-4, "完善信息sql出错");
        }
    }


    @RequestMapping(value = "/uLogin", method = RequestMethod.POST)
    public R userLogin(@RequestBody String params) {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");

        try {
            Subject subject = ShiroUtil.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(399, "账号不存在");
        } catch (IncorrectCredentialsException e) {
            return R.error(399, "账号密码不正确");
        } catch (AuthenticationException e) {
            return R.error(399, "认证失败");
        }
        return R.ok("登录成功");
    }


    /**
     * Method sendLoginVerCode
     *
     * @param params  (类型：String)
     * @param request (类型：HttpServletRequest)
     * @return R
     * @throws GlassesException when
     * @author luxiaobo
     * @date 2019/5/29
     * <p>
     * 发送登录所用验证码
     */
    @RequestMapping(value = "/sendLoginVerCode", method = RequestMethod.POST)
    public R sendLoginVerCode(@RequestBody String params, HttpServletRequest request) throws GlassesException {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");

        //查看手机号是否被注册
        if (StringUtils.isEmpty(phone)||!userService.isPhoneExist(phone)) {
            return R.error(303, "该手机号尚未注册");
        }

        int result=userService.sendVerCode(phone, IPAddressUtil.getClientIpAddress(request), Constant.LOGIN_VERCODE_PREFIX);
        if (0==result){
            return R.ok("短信验证码发送成功");
        }else {
            return R.error(-6,"短信验证码发送失败");
        }
    }


    /**
     * Method phoneLogin
     *
     * @param params (类型：String)
     * @return R
     * @author luxiaobo
     * @date 2019/5/29
     * <p>
     * 手机号验证码登录校验，先进行验证码判断是否正确，再校验令牌PhoneToken是否正确
     */
    @RequestMapping(value = "/pLogin", method = RequestMethod.POST)
    public R phoneLogin(@RequestBody String params) {
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json数据取出data
        String phone = jsonObject.getString("phone");
        String verCode = jsonObject.getString("loginVerCode");
        //从redis那里获取验证码
        String vercodeFromRedis = redis.getCacheValue(Constant.LOGIN_VERCODE_PREFIX + phone);
        //判断从redis取来的验证码是否为空
        if (StringUtils.isEmpty(vercodeFromRedis)) {
            return R.error(-5, "redis出错");
        }
        //校验用户输入的验证码是否正确
        if (!vercodeFromRedis.equals(verCode)) {
            return R.error(399, "验证码输入有错");
        }

        try {
            Subject subject = ShiroUtil.getSubject();
            PhoneToken token = new PhoneToken(phone);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(399, "账号不存在");
        } catch (IncorrectCredentialsException e) {
            return R.error(399, "账号密码不正确");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return R.error(399, "认证失败");
        }
        return R.ok("登录成功");
    }


    /**
     * Method logout
     *
     *@author luxiaobo
     *@date 2019/5/29
     *
     * @return R
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public R logout(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }

        ShiroUtil.logout();
        return R.ok("退出成功");
    }




}
