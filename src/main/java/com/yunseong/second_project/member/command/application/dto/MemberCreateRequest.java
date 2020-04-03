package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {

    @Size(min = 6, max = 12)
    private String username;
    @Size(min = 8, max = 20)
    private String password;
    @Size(min = 4, max = 8)
    private String nickname;
}
