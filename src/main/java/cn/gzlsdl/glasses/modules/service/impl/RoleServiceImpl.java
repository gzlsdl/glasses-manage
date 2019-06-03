package cn.gzlsdl.glasses.modules.service.impl;

import cn.gzlsdl.glasses.common.constant.Constant;
import cn.gzlsdl.glasses.common.util.PageUtil;
import cn.gzlsdl.glasses.common.util.Query;
import cn.gzlsdl.glasses.modules.entity.Dept;
import cn.gzlsdl.glasses.modules.entity.Role;
import cn.gzlsdl.glasses.modules.dao.RoleMapper;
import cn.gzlsdl.glasses.modules.entity.RoleMenu;
import cn.gzlsdl.glasses.modules.service.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleDeptService roleDeptService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public PageUtil queryPage(JSONObject jsonObject) {
        String roleName=jsonObject.getString("roleName");

        Page<Role> page=this.selectPage(
                new Query<Role>(jsonObject).getPage(),
                new EntityWrapper<Role>()
                .like(StringUtils.isNotBlank(roleName),"role_name",roleName)
                .addFilterIfNeed(jsonObject.get(Constant.SQL_FILTER)!=null, (String) jsonObject.get(Constant.SQL_FILTER))
        );

        for (Role role:page.getRecords()){
            Dept dept=deptService.selectById(role.getDeptId());
            if (dept!=null){
                role.setDeptName(dept.getDeptName());
            }
        }

        return new PageUtil(page);
    }

    @Override
    public List<Long> queryMenuIdList(long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    @Override
    public List<Long> queryDeptIdList(long roleId) {
        return baseMapper.queryDeptIdList(roleId);
    }

    @Override
    public void save(Role role) {
        role.setCreateTime(new Date());
        this.insert(role);

        //保存角色与部门关系
        roleDeptService.iOrUToRoleDept(role.getId(),role.getDeptIdList());

        //保存角色与菜单关系
        roleMenuService.iOrURoleMenu(role.getId(),role.getMenuIdList());
    }


    @Override
    public void update(Role role) {
        this.updateById(role);

        //更新角色与部门关系
        roleDeptService.iOrUToRoleDept(role.getId(),role.getDeptIdList());

        //保存角色与菜单关系
        roleMenuService.iOrURoleMenu(role.getId(),role.getMenuIdList());
    }


    @Override
    public void deleteIds(Long[] roleIds) {
        //先删除角色
        this.deleteBatchIds(Arrays.asList(roleIds));

        //删除角色与部门关联
        roleDeptService.deleteBatch(roleIds);

        //删除角色与菜单关联
        roleMenuService.deleteBatch(roleIds);

        //删除用户与角色关联
        userRoleService.deleteBatch(roleIds);
    }


}
