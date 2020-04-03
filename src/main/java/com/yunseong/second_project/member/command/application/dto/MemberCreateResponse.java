package com.yunseong.second_project.member.command.application.dto;

import com.yunseong.second_project.member.command.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberCreateResponse {

    private String username;
    private String password;
    private String nickname;
    private String grade;
    private LocalDateTime createTime;

    public MemberCreateResponse(Member member) {
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.grade = member.getGrade().getValue();
        this.createTime = member.getCreatedTime();
    }
}
