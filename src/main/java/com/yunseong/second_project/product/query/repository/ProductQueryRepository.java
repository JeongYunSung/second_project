package com.yunseong.second_project.product.query.repository;

import com.yunseong.second_project.product.query.application.dto.ProductResponse;
import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {

    Page<ProductResponse> findPageBySearch(ProductSearchCondition condition, Pageable pageable);
}
