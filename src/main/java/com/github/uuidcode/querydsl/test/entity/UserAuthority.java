package com.github.uuidcode.querydsl.test.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;
    private Long userId;
    private String authority;
    private Date regDatetime;
    private Date modDatetime;

    public static UserAuthority of() {
        return new UserAuthority();
    }

    public Long getAuthorityId() {
        return this.authorityId;
    }

    public UserAuthority setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserAuthority setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getAuthority() {
        return this.authority;
    }

    public UserAuthority setAuthority(String authority) {
        this.authority = authority;
        return this;
    }

    public Date getRegDatetime() {
        return this.regDatetime;
    }

    public UserAuthority setRegDatetime(Date regDatetime) {
        this.regDatetime = regDatetime;
        return this;
    }

    public Date getModDatetime() {
        return this.modDatetime;
    }

    public UserAuthority setModDatetime(Date modDatetime) {
        this.modDatetime = modDatetime;
        return this;
    }
}