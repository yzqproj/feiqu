package com.feiqu.system.pojo.cache;

import com.feiqu.system.model.FqUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */
@Data
public class FqUserCache implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Date createTime;
    private String nickname;
    private String icon;
    private Integer sex;
    private Integer isSingle;
    private Integer isMailBind;
    private String sign;
    private String city;
    private String username;
    private Integer qudouNum;
    private String birth;
    private String education;
    private String school;
    private Integer role;
    private Integer status;
    private Integer level;

    public FqUserCache() {
    }

    public FqUserCache(FqUser fqUser) {
        this.setCity(fqUser.getCity());
        this.setCreateTime(fqUser.getCreateTime());
        this.setIcon(fqUser.getIcon());
        this.setId(fqUser.getId());
        this.setIsMailBind(fqUser.getIsMailBind());
        this.setIsSingle(fqUser.getIsSingle());
        this.setSex(fqUser.getSex());
        this.setSign(fqUser.getSign());
        this.setNickname(fqUser.getNickname());
        this.setUsername(fqUser.getUsername());
        this.setQudouNum(fqUser.getQudouNum());
        this.setBirth(fqUser.getBirth());
        this.setEducation(fqUser.getEducation());
        this.setSchool(fqUser.getSchool());
        this.setRole(fqUser.getRole());
        this.setStatus(fqUser.getStatus());
        this.setLevel(fqUser.getLevel());
    }

    public FqUser transferFqUser(){
        FqUser fqUser = new FqUser();
        fqUser.setCity(this.getCity());
        fqUser.setCreateTime(this.getCreateTime());
        fqUser.setIcon(this.getIcon());
        fqUser.setId(this.getId());
        fqUser.setIsMailBind(this.getIsMailBind());
        fqUser.setIsSingle(this.getIsSingle());
        fqUser.setSex(this.getSex());
        fqUser.setSign(this.getSign());
        fqUser.setNickname(this.getNickname());
        fqUser.setUsername(this.getUsername());
        fqUser.setQudouNum(this.getQudouNum());
        fqUser.setBirth(this.getBirth());
        fqUser.setEducation(this.getEducation());
        fqUser.setSchool(this.getSchool());
        fqUser.setRole(this.getRole());
        fqUser.setStatus(this.status);
        fqUser.setLevel(this.level);
        return fqUser;
    }

}
