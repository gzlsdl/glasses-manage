package cn.gzlsdl.glasses.modules.entity;




import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-21
 */
@TableName("sys_role")
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("role_name")
    private String roleName;
    private String remark;
    @TableField("dept_id")
    private Long deptId;
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private List<Long> menuIdList;
    @TableField(exist = false)
    private List<Long> deptIdList;



}
