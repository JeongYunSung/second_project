package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class MemberUpdateRequest {

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @Size(min = 8, max = 20)
    private String verifyPassword;
    @NotBlank
    @Size(min = 4, max = 8)
    private String nickname;
    @NotNull
    @Min(0) @Max(Integer.MAX_VALUE)
    private int money;
}
