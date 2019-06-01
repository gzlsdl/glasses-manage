package cn.gzlsdl.glasses.modules.service;

import cn.gzlsdl.glasses.modules.entity.Dept;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
public interface DeptService extends IService<Dept> {

    List<Dept> queryList(Map<String,Object> map);

    List<Long> queryDeptIdList(long deptId);
}
