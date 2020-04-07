package com.yunseong.second_project.category.query;

import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse findCategory(Long id) {
        CategoryResponse category = this.categoryRepository.findDtoById(id).orElseThrow(() -> new NotFoundEntityException("해당 카테고리는 존재하지 않습니다.", id));
        return category;
    }

    public Page<CategoryResponse> findCategoryByPage(Pageable pageable) {
        return this.categoryRepository.findAllByPage(pageable);
    }
}
