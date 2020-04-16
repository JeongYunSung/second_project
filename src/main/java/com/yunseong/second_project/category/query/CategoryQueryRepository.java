package com.yunseong.second_project.category.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryQueryRepository {

    Page<CategoryResponse> findAllByPage(Pageable pageable);
}
