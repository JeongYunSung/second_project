package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberCreateResponse {

    private String username;
    private String password;
    private String nickname;
    private LocalDateTime createTime;
}
