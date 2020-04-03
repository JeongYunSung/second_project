package com.yunseong.second_project.member.command.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "purchase_id"))
public class Purchase extends BaseUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long productId;

    private String productName;

    public Purchase(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
