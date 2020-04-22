package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.dto.MemberUpdateRequest;
import com.yunseong.second_project.member.query.MemberQueryService;
import com.yunseong.second_project.member.query.dto.MemberQueryResponse;
import com.yunseong.second_project.member.query.dto.model.MemberQueryResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/members/me", produces = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8")
@RequiredArgsConstructor
public class MemberMeController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    public ResponseEntity findMember() {
        MemberQueryResponse memberResponse = this.memberQueryService.findMember(((CustomUser)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId());
        MemberQueryResponseModel model = new MemberQueryResponseModel(memberResponse);
        model.add(Util.profile);
        return ResponseEntity.ok(model);
    }

    @PutMapping
    public ResponseEntity putMember(@RequestBody MemberUpdateRequest request, Errors errors) {
        return Util.getUtil().getUpdateResponseEntity(errors, this.memberCommandService.updatePutMember(request));
    }

/*    @PatchMapping
    public ResponseEntity patchMember(@RequestBody MemberUpdateRequest request, Errors errors) {
        this.memberUpdatePatchRequestValidator.validate(request, errors);
        return Util.getUtil().getUpdateResponseEntity(errors, this.memberCommandService.updatePatchMember(request));
    }*/

    @DeleteMapping
    public ResponseEntity deleteMember() {
        this.memberCommandService.deleteMember();
        return ResponseEntity.noContent().build();
    }
}
