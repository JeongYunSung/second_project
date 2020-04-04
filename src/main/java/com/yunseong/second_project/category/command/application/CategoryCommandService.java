package com.yunseong.second_project.category.command.application;

import com.yunseong.second_project.category.command.application.dto.CategoryCreateRequest;
import com.yunseong.second_project.category.command.application.dto.CategoryCreateResponse;
import com.yunseong.second_project.category.command.application.dto.CategoryUpdateRequest;
import com.yunseong.second_project.category.command.application.dto.CategoryUpdateResponse;
import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public CategoryCreateResponse registerCategory(CategoryCreateRequest request) {
        Category parent = getCategory(request.getParentId());
        Category category = new Category(request.getCategoryName(), parent);
        this.categoryRepository.save(category);
        return new CategoryCreateResponse(category);
    }

    public CategoryUpdateResponse updatePutCategory(Long id, CategoryUpdateRequest request) {
        Category parent = getCategory(request.getParentId());
        Category category = getCategory(id);
        category.changeName(request.getCategoryName());
        category.setParent(parent);
        CategoryUpdateResponse response = new CategoryUpdateResponse(category)
                .withId(category.getId())
                .withCategoryName(category.getCategoryName());
        if (parent != null) {
            response.withParentId(parent.getId()).withParentName(parent.getCategoryName());
        }
        return response;
    }

    public CategoryUpdateResponse updatePatchCategory(Long id, CategoryUpdateRequest request) {
        Category category = getCategory(id);
        CategoryUpdateResponse response = new CategoryUpdateResponse(category);
        if (request.getParentId() != null) {
            Category parent = getCategory(request.getParentId());
            response.withParentId(parent.getId());
            response.withParentName(parent.getCategoryName());
        }
        if (StringUtils.hasText(request.getCategoryName())) {
            category.changeName(request.getCategoryName());
            response.withId(category.getId());
            response.withCategoryName(category.getCategoryName());
        }
        return response;
    }

    public void deleteCategory(Long id) {
        Category category = getCategory(id);
        category.delete();
    }

    private Category getCategory(Long id) {
        return this.categoryRepository.findFetchById(id).orElseThrow(() -> new NotFoundEntityException("해당 카테고리엔티티는 존재하지 않습니다.", id));
    }
}
