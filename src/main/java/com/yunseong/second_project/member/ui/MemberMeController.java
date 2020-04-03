package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.dto.MemberUpdateRequest;
import com.yunseong.second_project.member.command.application.dto.MemberUpdateResponse;
import com.yunseong.second_project.member.query.MemberQueryService;
import com.yunseong.second_project.member.query.dto.MemberQueryResponse;
import com.yunseong.second_project.member.query.dto.PurchaseResponse;
import com.yunseong.second_project.member.query.dto.model.MemberQueryResponseModel;
import com.yunseong.second_project.member.query.dto.model.PurchaseResponseModel;
import com.yunseong.second_project.member.ui.validator.MemberUpdateRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/v1/members/me", consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class MemberMeController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final MemberUpdateRequestValidator memberUpdateRequestValidator;

    @GetMapping
    public ResponseEntity findMember() {
        MemberQueryResponse memberResponse = this.memberQueryService.findMember(((CustomUser)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId());
        List<PurchaseResponseModel> products = memberResponse.getPurchase().stream().map(PurchaseResponse::new).map(purchaseResponse -> new PurchaseResponseModel(purchaseResponse,
                linkTo(MemberMeController.class).withRel("product"))).collect(Collectors.toList());
        MemberQueryResponseModel model = new MemberQueryResponseModel(memberResponse, products);
        model.add(linkTo(MemberMeController.class).withSelfRel());
        model.add(Util.profile);
        return ResponseEntity.ok(model);
    }

    @PutMapping
    public ResponseEntity putMember(@RequestBody  MemberUpdateRequest request, Errors errors) {
        return Util.getUtil().getUpdateResponseEntity(errors, this.memberCommandService.updatePutMember(request));
    }

    @PatchMapping
    public ResponseEntity patchMember(@RequestBody  MemberUpdateRequest request, Errors errors) {
        this.memberUpdateRequestValidator.validate(request, errors);
        return Util.getUtil().getUpdateResponseEntity(errors, this.memberCommandService.updatePatchMember(request));
    }

    @DeleteMapping
    public ResponseEntity deleteMember() {
        this.memberCommandService.deleteMember();
        return ResponseEntity.noContent().build();
    }
}
