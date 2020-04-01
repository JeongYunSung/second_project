package com.yunseong.second_project.member.command.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import com.yunseong.second_project.product.command.domain.Product;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Purchase(Product product) {
        this.product = product;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
