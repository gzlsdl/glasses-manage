package cn.gzlsdl.glasses.modules.entity;


import java.util.Date;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@TableName("sys_user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String phone;
    @TableField("real_name")
    private String realName;
    private String password;
    private String email;
    private String sex;
    private String salt;
    private Integer status;
    @TableField("dept_id")
    private Long deptId;
    @TableField("create_time")
    private Date createTime;
    @TableField("head_address")
    private String headAddress;
    private String company;
    private String province;
    private String size;
    private String city;

    @TableField(exist = false)
    private String deptName;



}
