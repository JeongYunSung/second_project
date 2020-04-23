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
                .where(product.delete_yn.eq(false), containsAll(condition.getCategory(), condition.getProduct()),
                        loeValue(condition.getMax()), goeValue(condition.getMin()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return getPage(content, pageable,
                this.queryFactory.select()
                        .select(new QProductResponse(product))
                        .from(product)
                        .innerJoin(product.types, productType)
                        .innerJoin(productType.type, type)
                        .where(product.delete_yn.eq(false), containsAll(condition.getCategory(), condition.getProduct()),
                                loeValue(condition.getMax()), goeValue(condition.getMin()))::fetchCount);
    }

    public BooleanExpression containsAll(String categoryName, String productName) {
        BooleanExpression category = this.containsCategoryName(categoryName);
        BooleanExpression product = this.containsProductName(productName);
        if(category == null && product == null) {
            return null;
        }
        if(category == null) {
            return product;
        }
        if(product == null) {
            return category;
        }
        return category.or(product);
    }

    public BooleanExpression goeValue(String value) {
        return hasText(value) && !value.equalsIgnoreCase("null") ? product.value.goe(Integer.parseInt(value)) : null;
    }

    public BooleanExpression loeValue(String value) {
        return hasText(value) && !value.equalsIgnoreCase("null") ? product.value.loe(Integer.parseInt(value)) : null;
    }

    public BooleanExpression containsCategoryName(String categoryName) {
        return hasText(categoryName) && !categoryName.equalsIgnoreCase("null") ? type.categoryName.lower().contains(categoryName.toLowerCase()) : null;
    }

    public BooleanExpression containsProductName(String productName) {
        return hasText(productName) && !productName.equalsIgnoreCase("null") ? product.productName.lower().contains(productName.toLowerCase()) : null;
    }
}
