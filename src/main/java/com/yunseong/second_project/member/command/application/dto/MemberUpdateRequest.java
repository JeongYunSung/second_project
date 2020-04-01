package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberUpdateRequest {

    private String password;
    private String verifyPassword;
    private String nickname;
    private Integer money;
    private List<Long> purchaseIdList;
}
