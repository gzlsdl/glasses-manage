package cn.gzlsdl.glasses.modules.controller;


import cn.gzlsdl.glasses.common.result.R;
import cn.gzlsdl.glasses.common.util.BaseController;
import cn.gzlsdl.glasses.common.util.JsonToEntity;
import cn.gzlsdl.glasses.modules.entity.Dept;
import cn.gzlsdl.glasses.modules.service.DeptService;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Class DeptController
 * Description:部门控制器
 * @author luxiaobo
 * Created on 2019/5/31
 */
@RestController
@RequestMapping("/sys/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @RequiresPermissions(value = "sys:dept:list")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public R list(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        List<Dept> deptList=deptService.queryList(jsonObject);
        return R.ok().put("list",deptList);
    }


    @RequiresPermissions(value = "sys:dept:select")
    @RequestMapping(value = "/select",method = RequestMethod.POST)
    public R select(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }

        List<Dept> deptList=deptService.queryList(jsonObject);
        return R.ok().put("deptList",deptList);
    }



    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @RequiresPermissions(value = "sys:dept:create")
    public R create(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        Dept dept= JsonToEntity.jsonToDept(jsonObject);
        boolean flag=deptService.insert(dept);
        if (flag){
            return R.ok("操作成功");
        }else {
            return R.error(-4,"数据库操作失败");
        }
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions(value = "sys:dept:update")
    public R update(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.error(-1,"sign签名校验失败");
        }
        Dept dept=JsonToEntity.jsonToDept(jsonObject);
        boolean flag = deptService.updateById(dept);
        if (flag){
            return R.ok("操作成功");
        }else {
            return R.error(-4,"数据库操作失败");
        }

    }


    @RequiresPermissions(value = "sys:dept:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public R delete(@RequestBody String params){
        JSONObject jsonObject=checkParams(params);
        if (jsonObject==null){
            return R.sign();
        }
        //根据部门id查询其是否有子部门id
        long deptId=JsonToEntity.jsonToDeptId(jsonObject);
        List<Long> deptIdList=deptService.queryDeptIdList(deptId);
        if (deptIdList.size()>0){
            return R.error(399,"请先删除子部门");
        }
        boolean flag = deptService.deleteById(deptId);
        if (flag){
            return R.ok();
        }
        return R.mysql();
    }

}

