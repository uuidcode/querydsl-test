package com.github.uuidcode.querydsl.test.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.QUserAuthority;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.entity.UserAuthority;

@Service
public class UserAuthorityService extends EntityService<UserAuthority> {
    public List<UserAuthority> list(Long userId) {
        return this.createQuery()
            .select(QUserAuthority.userAuthority)
            .from(QUserAuthority.userAuthority)
            .where(QUserAuthority.userAuthority.userId.eq(userId))
            .fetch();
    }

    public void set(List<User> userList)  {
        if (userList == null) {
            return;
        }

        Map<Long, User> userMap = userList.stream()
            .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<Long> userIdList = userMap.keySet()
            .stream()
            .collect(Collectors.toList());

        List<UserAuthority> userAuthorityList = this.createQuery()
            .select(QUserAuthority.userAuthority)
            .from(QUserAuthority.userAuthority)
            .where(QUserAuthority.userAuthority.userId.in(userIdList))
            .fetch();

        userAuthorityList
            .forEach(userAuthority -> {
                User user = userMap.get(userAuthority.getUserId());

                if (user != null) {
                    user.addUserAuthority(userAuthority);
                }
            });
    }
}
