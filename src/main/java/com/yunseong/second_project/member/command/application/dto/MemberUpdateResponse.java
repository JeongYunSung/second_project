package com.yunseong.second_project.member.command.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunseong.second_project.member.command.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MemberUpdateResponse {

    private @With String password;
    private @With String nickname;
    private @With Integer money;
    private @With String grade;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public MemberUpdateResponse(Member member) {
        this.createdTime = member.getCreatedTime();
        this.updatedTime = member.getUpdatedTime();
    }
}
