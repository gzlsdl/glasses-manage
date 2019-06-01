package cn.gzlsdl.glasses.modules.entity;




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
@TableName("sys_dept")
@Data
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("dept_name")
    private String deptName;
    @TableField("parent_id")
    private Long parentId;
    private String remark;
    @TableField(exist=false)
    private String parentName;



}
