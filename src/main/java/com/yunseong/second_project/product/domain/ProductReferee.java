package com.yunseong.second_project.product.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "productreferee_id"))
public class ProductReferee extends BaseUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_name")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_name")
    private Referee referee;

    public ProductReferee(Referee referee) {
        this.referee = referee;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
