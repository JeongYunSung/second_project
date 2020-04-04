package com.yunseong.second_project.category.command.domain;

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
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseUserEntity {

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    public Category(String categoryName, Category parent) {
        this.categoryName = categoryName;
        this.parent = parent;
    }

    public void changeName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setParent(Category parent) {
        if (this.getParent().getCategories().contains(this))
            this.getParent().getCategories().remove(this);
        this.parent = parent;
        parent.addChild(this);
    }

    private void addChild(Category child) {
        this.getParent().addChild(child);
    }

    @Override
    public void delete() {
        super.delete();
        this.getCategories().forEach(Category::delete);
    }
}