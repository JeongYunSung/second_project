package com.yunseong.second_project.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberUpdateRequest {

    @Min(value = 8) @Max(value = 20)
    private String password;
    @Min(value = 8) @Max(value = 20)
    private String verifyPassword;
    @Min(value = 4) @Max(value = 8)
    private String nickname;
    @Min(value = 0) @Max(value = Integer.MAX_VALUE)
    private Integer money;
    private List<Long> purchaseIdList;
}
