package com.yunseong.second_project.category.command.domain;

import com.yunseong.second_project.category.query.CategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {
}
