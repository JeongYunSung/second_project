package com.yunseong.second_project.product.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseUserEntity {

    private String productName;
    private String description;
    private Integer value;
    private Integer view;

    @OneToMany(mappedBy = "product")
    private List<ProductReferee> productReferees = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Type> types = new ArrayList<>();

    public Product(String productName, String description, Integer value, List<Type> list) {
        this.productName = productName;
        this.description = description;
        this.value = value;
        this.types.addAll(list);
    }

    public void addType(Type type) {
        this.getTypes().add(type);
        type.setProduct(this);
    }

    public void addReferee(ProductReferee referee) {
        this.getProductReferees().add(referee);
        referee.setProduct(this);
    }

    public void addView() {
        this.view += 1;
    }
}
