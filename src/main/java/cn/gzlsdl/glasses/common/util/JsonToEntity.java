package cn.gzlsdl.glasses.common.util;

import cn.gzlsdl.glasses.modules.entity.Dept;
import cn.gzlsdl.glasses.modules.entity.Role;
import cn.gzlsdl.glasses.modules.entity.User;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;


/**
 * Class JsonToEntity
 * Description:将json格式的数据转化为实体
 * @author luxiaobo
 * Created on 2019/6/1
 */
public class JsonToEntity {

    public static User jsonToUser(JSONObject jsonObject){
        String realName = jsonObject.getString("realName");
        String memberId = jsonObject.getString("memberId");
        String sex = jsonObject.getString("sex");
        String phone = jsonObject.getString("phone");
        User user = new User();
        user.setPhone(phone);
        user.setSex(sex);
        user.setRealName(realName);
        return user;
    }

    public static Dept jsonToDept(JSONObject jsonObject) {
        String deptName=jsonObject.getString("deptName");
        String parentId=jsonObject.getString("parentId");
        long pId=Long.parseLong(parentId);
        String deptId=jsonObject.getString("deptId");
//        String orderNum=jsonObject.getString("order_num");
        Dept dept = new Dept();
        dept.setDeptName(deptName);
        dept.setParentId(pId);
        return dept;
    }

    public static long jsonToDeptId(JSONObject jsonObject) {
        String deptId=jsonObject.getString("deptId");
        long dId=Long.parseLong(deptId);
        return dId;
    }

    public static Role jsonToRole(JSONObject jsonObject) {
        String roleName=jsonObject.getString("roleName");
        String deptId=jsonObject.getString("deptId");
        long dId=Long.parseLong(deptId);
        String remark=jsonObject.getString("remark");

        Role role = new Role();
        role.setRoleName(roleName);
        role.setDeptId(dId);
        role.setRemark(remark);
        if (jsonObject.getString("roleId")!=null){
            String roleId=jsonObject.getString("roleId");
            long id=Long.parseLong(roleId);
            role.setId(id);
        }
        return role;
    }
}
