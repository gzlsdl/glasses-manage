package cn.gzlsdl.glasses.modules.service;

import cn.gzlsdl.glasses.common.util.PageUtil;
import cn.gzlsdl.glasses.modules.entity.Role;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
public interface RoleService extends IService<Role> {

    PageUtil queryPage(JSONObject jsonObject);

    List<Long> queryMenuIdList(long roleId);

    List<Long> queryDeptIdList(long roleId);

    void save(Role role);


    void deleteIds(Long[] roleIds);

    void update(Role role);

}
