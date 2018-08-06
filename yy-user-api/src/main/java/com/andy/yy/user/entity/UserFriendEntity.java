package com.andy.yy.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 用户好友表
 * @author richard
 * @since 2018-02-09 11:18:51
 */
public class UserFriendEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;                         // 主键
    private Long userId;                     // 申请用户id
    private Long friendId;                   // 好友id
    private String friendRemark;             // 好友备注
    private String status;                   // 状态 通过:PASS 审核:AUDIT 拒绝:REFUSE 拉黑:BLACK
    private Date createTime;                 // 创建时间
    private Date updateTime;                 // 修改时间

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getFriendId() {
        return friendId;
    }
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    public String getFriendRemark() {
        return friendRemark;
    }
    public void setFriendRemark(String friendRemark) {
        this.friendRemark = friendRemark == null ? null : friendRemark.trim();
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