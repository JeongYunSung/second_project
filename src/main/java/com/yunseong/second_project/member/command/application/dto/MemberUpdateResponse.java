package com.yunseong.second_project.member.command.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class MemberUpdateResponse {

    private @With String password;
    private @With String nickname;
    private @With Integer money;
    private @With List<Long> purchaseIdList;
    private final LocalDateTime createdTime;
    private final LocalDateTime updatedTime;
}
