package cn.gzlsdl.glasses.modules.dao;

import cn.gzlsdl.glasses.modules.entity.Dept;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
public interface DeptMapper extends BaseMapper<Dept> {

    List<Dept> queryList(Map<String,Object> params);

    List<Long> queryDeptSubIdList(Long parentId);

}
