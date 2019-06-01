package cn.gzlsdl.glasses.modules.service.impl;

import cn.gzlsdl.glasses.modules.entity.Dept;
import cn.gzlsdl.glasses.modules.dao.DeptMapper;
import cn.gzlsdl.glasses.modules.service.DeptService;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public List<Dept> queryList(Map<String, Object> map) {
        return baseMapper.queryList(map);
    }

    @Override
    public List<Long> queryDeptIdList(long deptId) {
        return baseMapper.queryDeptSubIdList(deptId);
    }
}
