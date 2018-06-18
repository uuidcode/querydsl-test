package com.github.uuidcode.querydsl.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.ContentCount;
import com.github.uuidcode.querydsl.test.entity.QContentCount;
import com.github.uuidcode.querydsl.test.entity.User;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ContentCountService extends EntityService<ContentCount> {
    public List<ContentCount> list(ContentCount contentCount) {
        return this.createQuery()
            .select(QContentCount.contentCount)
            .from(QContentCount.contentCount)
            .where(QContentCount.contentCount.contentType.eq(contentCount.getContentType()))
            .where(QContentCount.contentCount.contentId.in(contentCount.getContentIdList()))
            .fetch();
    }

    public void manualJoin(List<User> userList) {
        if (userList == null) {
            return;
        }

        List<Long> userIdList = userList.stream()
            .map(User::getUserId)
            .collect(toList());

        ContentCount contentCount = ContentCount.of()
            .setContentType(ContentCount.ContentType.USER)
            .setContentIdList(userIdList);

        List<ContentCount> contentCountList = this.list(contentCount);

        Map<Long, List<ContentCount>> contentCountMap = contentCountList.stream()
            .collect(groupingBy(ContentCount::getContentId));

        userList.forEach(user -> {
            List<ContentCount> countList = contentCountMap.get(user.getUserId());

            Map<ContentCount.CountType, Long> countMap = ofNullable(countList)
                .orElse(new ArrayList<>())
                .stream()
                .collect(toMap(ContentCount::getCountType, ContentCount::getCount));

            user.setContentCountMap(countMap);
        });
    }
}
