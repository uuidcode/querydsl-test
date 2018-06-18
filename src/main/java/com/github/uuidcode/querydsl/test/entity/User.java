package com.github.uuidcode.querydsl.test.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.github.uuidcode.querydsl.test.entity.ContentCount.CountType;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Date regDatetime;
    private Date modDatetime;
    @Transient
    private List<UserAuthority> userAuthorityList = new ArrayList<>();
    @Transient
    private List<Book> bookList = new ArrayList<>();
    @Transient
    private Map<CountType, Long> contentCountMap;

    public Map<CountType, Long> getContentCountMap() {
        return this.contentCountMap;
    }

    public User setContentCountMap(Map<CountType, Long> contentCountMap) {
        this.contentCountMap = contentCountMap;
        return this;
    }

    public User addBook(Book book) {
        this.bookList.add(book);
        return this;
    }

    public List<Book> getBookList() {
        return this.bookList;
    }

    public User setBookList(List<Book> bookList) {
        this.bookList = bookList;
        return this;
    }
    public static User of() {
        return new User();
    }

    public User setUserAuthorityList(List<UserAuthority> userAuthorityList) {
        this.userAuthorityList = userAuthorityList;
        return this;
    }

    public User addUserAuthority(UserAuthority userAuthority) {
        this.userAuthorityList.add(userAuthority);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getAccountNonExpired() {
        return this.accountNonExpired;
    }

    public User setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    public boolean getAccountNonLocked() {
        return this.accountNonLocked;
    }

    public User setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }

    public boolean getCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public User setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Date getRegDatetime() {
        return this.regDatetime;
    }

    public User setRegDatetime(Date regDatetime) {
        this.regDatetime = regDatetime;
        return this;
    }

    public Date getModDatetime() {
        return this.modDatetime;
    }

    public User setModDatetime(Date modDatetime) {
        this.modDatetime = modDatetime;
        return this;
    }

    public Collection<UserAuthority> getAuthorities() {
        return this.userAuthorityList;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public static void removePassword(Object principal) {
        if (principal instanceof User) {
            User user = (User) principal;
            user.setPassword(null);
        }
    }
}