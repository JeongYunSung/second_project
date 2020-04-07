package com.yunseong.second_project.member.command.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "memberpurchase_id"))
public class MemberPurchase extends BaseUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_name")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_name")
    private Purchase purchase;

    public MemberPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
