package cn.gzlsdl.glasses.modules.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@TableName("invite_code")
public class InviteCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String code;
    private Integer status;
    @TableField("register_user")
    private String registerUser;
    @TableField("create_time")
    private Date createTime;
    @TableField("register_time")
    private Date registerTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(String registerUser) {
        this.registerUser = registerUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "InviteCode{" +
        ", id=" + id +
        ", code=" + code +
        ", status=" + status +
        ", registerUser=" + registerUser +
        ", createTime=" + createTime +
        ", registerTime=" + registerTime +
        "}";
    }
}
