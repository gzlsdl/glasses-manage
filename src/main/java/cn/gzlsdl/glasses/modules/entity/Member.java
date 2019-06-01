package cn.gzlsdl.glasses.modules.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaobo
 * @since 2019-05-30
 */
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("member_name")
    private String memberName;
    private Integer level;
    @TableField("member_num")
    private Long memberNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Long memberNum) {
        this.memberNum = memberNum;
    }

    @Override
    public String toString() {
        return "Member{" +
        ", id=" + id +
        ", memberName=" + memberName +
        ", level=" + level +
        ", memberNum=" + memberNum +
        "}";
    }
}
