package com.example.springboot.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /** 盐  */
    private String salt;

    private String email;

    /** 手机号 */
    private String mobile;

    /** 状态  0：禁用   1：正常 */
    private Integer status;

    /** 创建者ID */
    private Long createUserId;

    /** 创建时间 */
    private Date createTime;

    /** 角色ID列表, 数据库没有对应属性 */
    @TableField(exist = false)
    private List<Long> roleIdList;
}
