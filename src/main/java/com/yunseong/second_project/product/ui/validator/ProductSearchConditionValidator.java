package com.yunseong.second_project.product.ui.validator;

import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.StringUtils.hasText;

@Component
public class ProductSearchConditionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductSearchCondition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductSearchCondition condition = (ProductSearchCondition) target;
        if (condition.getMin() == null && condition.getMax() == null && !hasText(condition.getCategoryName()) && !hasText(condition.getProductName())) {
            errors.reject("required", "search value is required");
        }
    }
}
