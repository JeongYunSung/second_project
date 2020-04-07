package com.yunseong.second_project.member.query.dto;

import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.Purchase;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class MemberQueryResponse {

    private String username;
    private String nickname;
    private Integer money;
    private Stream<PurchaseResponse> purchase;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public MemberQueryResponse(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.money = member.getMoney();
        this.purchase = member.getPurchases().stream().map(PurchaseResponse::new);
        this.createdTime = member.getCreatedTime();
        this.updatedTime = member.getUpdatedTime();
    }
}
