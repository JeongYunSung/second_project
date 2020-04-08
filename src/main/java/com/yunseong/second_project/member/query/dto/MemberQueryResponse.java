package com.yunseong.second_project.member.query.dto;

import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.Purchase;
import com.yunseong.second_project.member.query.dto.model.PurchaseResponseModel;
import com.yunseong.second_project.member.ui.MemberMeController;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
public class MemberQueryResponse {

    private String username;
    private String nickname;
    private Integer money;
    private String grade;
    private List<PurchaseResponseModel> purchase;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public MemberQueryResponse(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.money = member.getMoney();
        this.grade = member.getGrade().getValue();
        this.purchase = member.getPurchases().stream().map(PurchaseResponse::new).map(purchaseResponse -> new PurchaseResponseModel(purchaseResponse,
                linkTo(MemberMeController.class).withRel("product"))).collect(Collectors.toList());
        this.createdTime = member.getCreatedTime();
        this.updatedTime = member.getUpdatedTime();
    }
}
