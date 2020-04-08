package com.yunseong.second_project.category.command.domain;

import com.yunseong.second_project.category.query.CategoryQueryRepository;
import com.yunseong.second_project.category.query.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {

    @Query("select c from Category c left join fetch c.parent parent where c.id = :id and c.delete = false")
    Optional<Category> findFetchById(Long id);

    @Query("select new com.yunseong.second_project.category.query.CategoryResponse(c) from Category c left join c.parent parent left join c.categories child where c.id = :id and c.delete = false")
    Optional<CategoryResponse> findDtoById(Long id);
}