package com.yunseong.second_project.member.command.application.dto;

import com.yunseong.second_project.member.command.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberUpdateRequest {

    @Size(min = 8, max = 20)
    private String password;
    @Size(min = 8, max = 20)
    private String verifyPassword;
    @Size(min = 4, max = 8)
    private String nickname;
    @Min(value = 0) @Max(value = Integer.MAX_VALUE)
    private Integer money;
    private Grade grade;
    private List<Long> purchaseIdList;
}
