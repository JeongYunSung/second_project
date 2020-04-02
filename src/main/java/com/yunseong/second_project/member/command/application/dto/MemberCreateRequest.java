package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    @Min(value = 6) @Max(value = 12)
    private String username;
    @NotBlank
    @Min(value = 8) @Max(value = 20)
    private String password;
    @NotBlank
    @Min(value = 4) @Max(value = 8)
    private String nickname;
}
