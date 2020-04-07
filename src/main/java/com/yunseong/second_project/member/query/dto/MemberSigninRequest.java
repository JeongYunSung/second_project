package com.yunseong.second_project.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSigninRequest {

    private String username;
    private String password;
}
