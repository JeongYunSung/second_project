package com.yunseong.second_project.admin.ui.validator;

import com.yunseong.second_project.category.command.application.dto.CategoryUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryPatchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryUpdateRequest request = (CategoryUpdateRequest) target;

        if (request.getParentId() == null || request.getCategoryName() == null) {
            errors.reject("required", "All fields cannot be null");
        }
    }
}
