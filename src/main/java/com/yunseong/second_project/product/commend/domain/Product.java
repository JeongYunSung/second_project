package com.yunseong.second_project.product.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseUserEntity {

    private String productName;
    private String description;
    private int value;
    private int view;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReferee> productReferees = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductType> types = new ArrayList<>();

    public Product(String productName, String description, int value, List<ProductType> list) {
        this.productName = productName;
        this.description = description;
        this.value = value;
        list.forEach(this::addType);
    }

    public void changeName(String productName) {
        this.productName = productName;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void changeValue(int value) {
        this.value = value;
    }

    public void update(String name, String description, int value, List<ProductType> types) {
        this.changeName(name);
        this.changeDescription(description);
        this.changeValue(value);
        this.getTypes().clear();
        types.forEach(this::addType);
    }

    public void addType(ProductType type) {
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

    @Override
    public void delete() {
        super.delete();
        this.getProductReferees().stream().forEach(ProductReferee::delete);
        this.getTypes().stream().forEach(ProductType::delete);
    }
}
