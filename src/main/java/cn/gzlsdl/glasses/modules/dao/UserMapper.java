package cn.gzlsdl.glasses.modules.dao;

import cn.gzlsdl.glasses.modules.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<String> queryAllPerms(Long userId);




}
