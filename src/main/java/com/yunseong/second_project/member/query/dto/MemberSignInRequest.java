package com.yunseong.second_project.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignInRequest {

    private String username;
    private String password;
}
