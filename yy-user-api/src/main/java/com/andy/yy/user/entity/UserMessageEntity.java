package com.andy.yy.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 用户消息表
 * @author richard
 * @since 2018-02-28 16:58:13
 */
public class UserMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;                         // 主键
    private String type;                     // 消息类型 消息:MSG 添加好友:ADD_FRIEND
    private Long from;                       // 发送人
    private Long to;                         // 接收人
    private String content;                  // 消息内容(json格式)
    private String status;                   // 消息状态 已处理:DO 未处理:NOT
    private Date createTime;                 // 创建时间
    private Date updateTime;                 // 修改时间

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
    public Long getFrom() {
        return from;
    }
    public void setFrom(Long from) {
        this.from = from;
    }
    public Long getTo() {
        return to;
    }
    public void setTo(Long to) {
        this.to = to;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}