package com.example.springboot.shiro.entity;

// SysRoleEntity.java

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_role")
public class SysRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableId
    private Long roleId;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /** 备注 */
    private String remark;

    /** 创建者ID  */
    private Long createUserId;

    /** 创建时间 */
    private Date createTime;

    @TableField(exist=false)
    private List<Long> menuIdList;

}
