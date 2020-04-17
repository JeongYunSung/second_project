package com.yunseong.second_project.product.infra;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.second_project.product.query.application.dto.ProductResponse;
import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import com.yunseong.second_project.product.query.application.dto.QProductResponse;
import com.yunseong.second_project.product.query.repository.ProductQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.yunseong.second_project.product.commend.domain.QProduct.product;
import static com.yunseong.second_project.product.commend.domain.QProductReferee.productReferee;
import static com.yunseong.second_project.product.commend.domain.QProductType.productType;
import static com.yunseong.second_project.product.commend.domain.QReferee.referee;
import static com.yunseong.second_project.product.commend.domain.QType.type;
import static org.springframework.data.repository.support.PageableExecutionUtils.getPage;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ProductRepositoryImpl implements ProductQueryRepository {

    private JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<ProductResponse> findViewTop10() {
        List<ProductResponse> content = this.queryFactory
                .select(new QProductResponse(product))
                .from(product)
                .where(product.delete_yn.eq(false))
                .orderBy(product.view.desc())
                .offset(0)
                .limit(10)
                .fetch();

        return content;
    }

    @Override
    public List<ProductResponse> findBestTop10() {
        List<ProductResponse> content = this.queryFactory
                .select(new QProductResponse(product))
                .from(product)
                .innerJoin(product.productReferees, productReferee)
                .where(product.delete_yn.eq(false))
                .groupBy(product.id)
                .orderBy(product.id.count().desc())
                .offset(0)
                .limit(10)
                .fetch();

        return content;
    }

    @Override
    public List<ProductResponse> findRecentTop10() {
        List<ProductResponse> content = this.queryFactory
                .select(new QProductResponse(product))
                .from(product)
                .where(product.delete_yn.eq(false))
                .orderBy(product.createdTime.desc())
                .offset(0)
                .limit(10)
                .fetch();

        return content;
    }

    @Override
    public Page<ProductResponse> findPageBySearch(ProductSearchCondition condition, Pageable pageable) {
        List<ProductResponse> content = this.queryFactory
                .select(new QProductResponse(product)).distinct()
                .from(product)
                .leftJoin(product.productReferees, productReferee)
                .leftJoin(productReferee.referee, referee)
                .innerJoin(product.types, productType)
                .innerJoin(productType.type, type)
                .where(product.delete_yn.eq(false), containsProductName(condition.getProductName()), containsCategoryName(condition.getCategoryName()),
                        loeValue(condition.getMax()), goeValue(condition.getMin()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return getPage(content, pageable,
                this.queryFactory.select()
                        .select(new QProductResponse(product))
                        .from(product)
                        .where(containsProductName(condition.getProductName()), containsCategoryName(condition.getCategoryName()),
                                loeValue(condition.getMax()), goeValue(condition.getMin()))::fetchCount);
    }

    public BooleanExpression goeValue(Integer value) {
        return value != null ? product.value.goe(value) : null;
    }

    public BooleanExpression loeValue(Integer value) {
        return value != null ? product.value.loe(value) : null;
    }

    public BooleanExpression containsCategoryName(String categoryName) {
        return hasText(categoryName) ? type.categoryName.lower().contains(categoryName.toLowerCase()) : null;
    }

    public BooleanExpression containsProductName(String productName) {
        return hasText(productName) ? product.productName.lower().contains(productName.toLowerCase()) : null;
    }
}
