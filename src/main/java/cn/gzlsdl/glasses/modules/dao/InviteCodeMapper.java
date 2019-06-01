package cn.gzlsdl.glasses.modules.dao;

import cn.gzlsdl.glasses.modules.entity.InviteCode;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@Repository
public interface InviteCodeMapper extends BaseMapper<InviteCode> {

    void updateRegisterUser(@Param("inviteCode") String inviteCode,@Param("phone") String phone);

    void updateStatusByInviteCode(@Param("status") Integer status,@Param("inviteCode") String invite);

    String selectByCode(String inviteCode);
}
