package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    @Size(min = 4, max = 16)
    private String username;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @Size(min = 4, max = 8)
    private String nickname;
}
