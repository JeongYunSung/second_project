package com.yunseong.second_project.member.query.dto;

import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.Purchase;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberQueryResponse {

    private String username;
    private String nickname;
    private Integer money;
    private List<Purchase> purchase;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public MemberQueryResponse(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.money = member.getMoney();
        this.purchase = member.getPurchases();
        this.createdTime = member.getCreatedTime();
        this.updatedTime = member.getUpdatedTime();
    }
}
