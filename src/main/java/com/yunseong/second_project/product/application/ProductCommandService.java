package com.yunseong.second_project.product.application;

import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.product.application.dto.ProductCreateRequest;
import com.yunseong.second_project.product.application.dto.ProductCreateResponse;
import com.yunseong.second_project.product.application.dto.ProductUpdateRequest;
import com.yunseong.second_project.product.application.dto.ProductUpdateResponse;
import com.yunseong.second_project.product.domain.Product;
import com.yunseong.second_project.product.domain.ProductRepository;
import com.yunseong.second_project.product.domain.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.getProductName(), request.getDescription(), request.getValue(),
                request.getTypes().stream()
                        .map(id -> {
                            Category category = this.categoryRepository.findFetchById(id).orElseThrow(() -> new NotFoundEntityException("해당 카테고리엔티티는 존재하지 않습니다.", id));
                            if (category.getParent() != null) {
                                return new Type(category.getId(), category.getCategoryName(), null, null);
                            }
                            return new Type(category.getId(), category.getCategoryName(), category.getParent().getId(), category.getParent().getCategoryName());
                        }).collect(Collectors.toList()));

        this.productRepository.save(product);
        return new ProductCreateResponse(product);
    }

    public ProductUpdateResponse updatePutProduct(ProductUpdateRequest request) {
        

        return new ProductUpdateResponse();
    }
}
