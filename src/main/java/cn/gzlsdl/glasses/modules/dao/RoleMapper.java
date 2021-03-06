package cn.gzlsdl.glasses.modules.dao;

import cn.gzlsdl.glasses.modules.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
public interface RoleMapper extends BaseMapper<Role> {


    List<Long> queryMenuIdList(long roleId);

    List<Long> queryDeptIdList(long roleId);
}
