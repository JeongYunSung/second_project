package com.yunseong.second_project.member.ui.validator;

import com.yunseong.second_project.member.command.application.dto.MemberUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.StringUtils.hasText;

public class MemberUpdatePatchRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberUpdateRequest request = (MemberUpdateRequest) target;

        if (!hasText(request.getPassword()) && !hasText(request.getNickname()) && !hasText(request.getVerifyPassword()) && request.getMoney() == null
            && request.getGrade() == null && request.getPurchaseIdList() == null) {
            errors.reject("required", "All fields cannot be null");
        }
    }
}
