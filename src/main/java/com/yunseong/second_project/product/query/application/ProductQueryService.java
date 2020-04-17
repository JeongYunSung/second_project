package com.yunseong.second_project.product.query.application;

import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.product.commend.domain.ProductRepository;
import com.yunseong.second_project.product.query.application.dto.ProductResponse;
import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductResponse findProduct(Long id) {
        return this.productRepository.findDtoById(id).orElseThrow(() -> new NotFoundEntityException("해당 엔티티는 존재하지 않습니다.", id));
    }

    public Page<ProductResponse> findPageBySearch(ProductSearchCondition condition, Pageable pageable) {
        return this.productRepository.findPageBySearch(condition, pageable);
    }

    public Page<ProductResponse> findByPage(Pageable pageable) {
        return this.productRepository.findByPage(pageable);
    }

    public List<ProductResponse> findBestTop10() {
        return this.productRepository.findBestTop10();
    }

    public List<ProductResponse> findRecentTop10() {
        return this.productRepository.findRecentTop10();
    }

    public List<ProductResponse> findViewTop10() {
        return this.productRepository.findViewTop10();
    }
}
