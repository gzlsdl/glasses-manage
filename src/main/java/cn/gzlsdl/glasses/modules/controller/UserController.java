package cn.gzlsdl.glasses.modules.controller;


import cn.gzlsdl.glasses.common.result.R;
import cn.gzlsdl.glasses.common.util.*;
import cn.gzlsdl.glasses.modules.entity.User;
import cn.gzlsdl.glasses.modules.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Class UserController
 * Description:用户管理控制器
 * @author luxiaobo
 * Created on 2019/5/29
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;


    /**
     * Method list
     *
     *@author luxiaobo
     *@date 2019/6/3
     * 分页
     * @param params (类型：String)
     * @return R
     */
    @RequiresPermissions("sys:user:list")
    @RequestMapping(value = "/list")
    public R list(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json中获取data
        PageUtil pageData=userService.queryPage(jsonObject);

        return R.ok().put("page",pageData);
    }



    /**
     * Method info
     *
     *@author luxiaobo
     *@date 2019/6/3
     * 获取当前登录用户信息
     * @param params (类型：String)
     * @return R
     */
    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public R info(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        return R.ok().put("user",getUser());
    }



    /**
     * Method create
     *
     *@author luxiaobo
     *@date 2019/6/3
     * 新增用户
     * @param params (类型：String)
     * @return R
     */
    @RequiresPermissions("sys:user:create")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public R create(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json中获取data
        User user = JsonToEntity.jsonToUser(jsonObject);
        userService.save(user);
        return R.ok();
    }


    public R update(@RequestBody String parsms){
        JSONObject jsonObject=checkParams(parsms);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        //从json中取得data
        User user=JsonToEntity.jsonToUser(jsonObject);
        userService.update(user);
        return R.ok();
    }







}

