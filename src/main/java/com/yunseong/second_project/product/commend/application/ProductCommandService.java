package com.yunseong.second_project.product.commend.application;

import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.product.commend.application.dto.*;
import com.yunseong.second_project.product.commend.domain.*;
import com.yunseong.second_project.product.query.application.dto.RecommendResponse;
import com.yunseong.second_project.product.query.application.dto.TypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.getProductName(), request.getDescription(), request.getValue(),
                request.getTypes().stream()
                        .map(this::getType).collect(Collectors.toList()));
        this.productRepository.save(product);
        return new ProductCreateResponse(product);
    }

    public ProductUpdateResponse updatePutProduct(Long id, ProductUpdateRequest request) {
        Product product = this.getProduct(id, 2);
        product.update(request.getProductName(), request.getDescription(), request.getValue(),
                request.getCategoryId().stream().map(this::getType).collect(Collectors.toList()));

        return new ProductUpdateResponse(product.getId(), product.getCreatedTime(), product.getUpdatedTime())
                .withProductName(product.getProductName()).withDescription(product.getDescription())
                .withTypes(product.getTypes().stream().map(type -> new TypeResponse(type.getType())).collect(Collectors.toList()));
    }

/*    public ProductUpdateResponse updatePatchProduct(Long id, ProductUpdateRequest request) {
        Product product = this.getProduct(id);
        ProductUpdateResponse response = new ProductUpdateResponse(product.getId(), product.getCreatedTime(), product.getUpdatedTime());
        if (request.getProductName() != null) {
            product.changeName(request.getProductName());
            response.withProductName(product.getProductName());
        }
        if (request.getDescription() != null) {
            product.changeDescription(request.getDescription());
            response.withDescription(product.getDescription());
        }
        if (request.getValue() != null && request.getValue() > 0) {
            product.changeValue(request.getValue());
            response.withValue(product.getValue());
        }
        if (request.getCategoryId() != null && request.getCategoryId().size() > 0) {
            request.getCategoryId().stream().forEach(idx -> product.addType(getType(idx)));
            response.withTypes(product.getTypes().stream().map(TypeResponse::new).collect(Collectors.toList()));
        }

        return response;
    }*/

    public RecommendResponse updateRecommendProduct(Long id) {
        Product product = getProduct(id, 1);
        Long idx = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Member member = this.memberRepository.findById(idx).orElseThrow(() -> new NotFoundEntityException("해당 유저엔티티는 존재하지 않습니다.", idx));
        product.addReferee(new ProductReferee(new Referee(member.getUsername(), member.getNickname())));

        return new RecommendResponse(member.getUsername(), member.getNickname());
    }

    public void updateView(Long id) {
        Product product = getProduct(id, 0);
        product.addView();
    }

    public void delete(Long id) {
        Product product = getProduct(id, 0);
        product.delete();
    }

    private ProductType getType(Long idx) {
        Category category = this.getCategory(idx);
        ProductType type;
        if (category.getParent() != null) {
            type = new ProductType(new Type(category.getId(), category.getCategoryName(), category.getParent().getId(), category.getParent().getCategoryName()));
        } else {
            type = new ProductType(new Type(category.getId(), category.getCategoryName()));
        }
        return type;
    }

    private Category getCategory(Long id) {
        return this.categoryRepository.findFetchById(id).orElseThrow(() -> new NotFoundEntityException("해당 카테고리엔티티는 존재하지 않습니다.", id));
    }

    private Product getProduct(Long id, int value) {
        if (value == 0) {
            return this.productRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("해당 상품은 존재하지 않습니다.", id));
        }
        if (value == 1) {
            return this.productRepository.findRecommendById(id).orElseThrow(() -> new NotFoundEntityException("해당 상품은 존재하지 않습니다.", id));
        }
        return this.productRepository.findTypesById(id).orElseThrow(() -> new NotFoundEntityException("해당 상품은 존재하지 않습니다.", id));
    }
}
