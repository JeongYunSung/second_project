package com.yunseong.second_project.category.command.domain;

import com.yunseong.second_project.category.query.CategoryQueryRepository;
import com.yunseong.second_project.category.query.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQueryRepository {

    @Query("select c from Category c left join fetch c.parent parent where c.id = :id and c.delete_yn = false")
    Optional<Category> findFetchById(Long id);

    @Query("select distinct c from Category c left join fetch c.parent parent left join fetch c.categories child where c.id = :id and c.delete_yn = false")
    Optional<Category> findDtoById(Long id);
}
