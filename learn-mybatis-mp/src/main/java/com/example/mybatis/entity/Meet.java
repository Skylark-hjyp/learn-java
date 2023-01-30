/*
 * @Author: hjf
 * @Date: 2022-03-28 09:50:20
 * @LastEditTime: 2022-11-10 17:09:26
 * @Description: 会议实体表  
 */
package com.example.mybatis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Meet implements Serializable{
    // 会议ID
    private Integer meetId;

    // 会议时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date meetTime;

    // 会议地点
    private String meetPlace;

    // 会议主题
    private String meetTheme;

    // 汇报人姓名
    private String meetReporter;

    // 汇报人ID
    private Integer meetReporterId;

    // 会议所属角色
    private String meetBelongRole;

    // 最后更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatetime;

    // 单个附件对象
    private Attach attach;

    // 附件对象列表
    private List<Attach> attachs;
    
}

