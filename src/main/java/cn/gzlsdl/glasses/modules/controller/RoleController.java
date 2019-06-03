package cn.gzlsdl.glasses.modules.controller;


import cn.gzlsdl.glasses.common.result.R;
import cn.gzlsdl.glasses.common.util.BaseController;
import cn.gzlsdl.glasses.common.util.JsonToEntity;
import cn.gzlsdl.glasses.common.util.PageUtil;
import cn.gzlsdl.glasses.modules.entity.Role;
import cn.gzlsdl.glasses.modules.service.RoleService;
import com.alibaba.fastjson.JSONObject;
import javafx.geometry.Pos;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;


@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequiresPermissions(value = "sys:role:list")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public R list(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        PageUtil pageData=roleService.queryPage(jsonObject);

        return R.ok().put("page",pageData);
    }


    @RequiresPermissions(value = "sys:role:select")
    @RequestMapping(value = "/select",method = RequestMethod.POST)
    public R select(@RequestBody String params){
        List<Role> list=roleService.selectList(null);
        return R.ok().put("list",list);
    }


    @RequiresPermissions("sys:role:info")
    @RequestMapping(value = "/info/{roleId}")
    public R info(@RequestBody String params, @PathVariable long roleId){
        Role role=roleService.selectById(roleId);
        //查询角色对应的菜单关系
        List<Long> menuIdList=roleService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);
        //查询角色对应的部分关系
        List<Long> deptIdList=roleService.queryDeptIdList(roleId);
        role.setDeptIdList(deptIdList);

        return R.ok().put("role",role);
    }


    @RequiresPermissions("sys:role:create")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public R create(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        Role role= JsonToEntity.jsonToRole(jsonObject);
        roleService.save(role);
        return R.ok();
    }


    @RequiresPermissions("sys:role:update")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public R update(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        Role role=JsonToEntity.jsonToRole(jsonObject);
        roleService.update(role);
        return R.ok();
    }


    @RequiresPermissions("sys:role:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public R delete(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        Long[] roleIds= (Long[]) jsonObject.get("roleIds");
        roleService.deleteIds(roleIds);
        return R.ok();
    }


}

