package com.andy.yy.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 用户表
 * @author richard
 * @since 2018-01-30 16:04:54
 */
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;                         // 主键
    private String account;                  // 账号
    private String pwd;                      // 密码
    private String salt;                     // 密码盐
    private String email;                    // 邮箱
    private String nickName;                 // 昵称
    private String headImg;                  // 头像
    private String remark;                   // 备注
    private String status;                   // 状态 正常:NORMAL 禁用:DISABLED
    private String token;                    // 令牌
    private Date createTime;                 // 创建时间
    private Date updateTime;                 // 修改时间

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }
    public String getHeadImg() {
        return headImg;
    }
    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
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