package com.yunseong.second_project.category.command.domain;

import com.yunseong.second_project.category.query.CategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {

    @Query("select c from Category c join fetch c.parent p")
    Optional<Category> findFetchById(Long id);
}
