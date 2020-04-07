package com.yunseong.second_project.category.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunseong.second_project.category.command.domain.QCategory;
import com.yunseong.second_project.category.query.CategoryQueryRepository;
import com.yunseong.second_project.category.query.CategoryResponse;
import com.yunseong.second_project.category.query.QCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yunseong.second_project.category.command.domain.QCategory.category;

@Repository
public class CategoryRepositoryImpl implements CategoryQueryRepository {

    private JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CategoryResponse> findAllByPage(Pageable pageable) {
        QCategory parent = new QCategory("parent");
        QCategory child = new QCategory("child");
        List<CategoryResponse> content = this.queryFactory
                .select(new QCategoryResponse(category)).distinct()
                .from(category)
                .leftJoin(category.parent, parent)
                .leftJoin(category.categories, child)
                .where(category.delete.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(content, pageable,
                this.queryFactory.select(new QCategoryResponse(category)).from(category).where(category.delete.eq(false))::fetchCount);
    }
}
