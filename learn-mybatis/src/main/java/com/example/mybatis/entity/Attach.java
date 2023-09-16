package com.example.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Attach implements Serializable {
    // 附件ID
    private Integer attachId;

    // 所属会议ID
    private Integer meetId;

    // 附件名称
    private String attachName;

    // 上传时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    // 上传人ID
    private Integer uploadPeopleId;

    // 上传人姓名
    private String uploadPeopleName;

    // 标签
    private String tag;

    // 下载链接
    private String downloadUrl;

    // 附件所属角色
    private String belongRole;

    // 下载次数
    private Integer downloadCount;

    // 附件类型
    private String type;

    // 最后更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatetime;


}
