package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.command.application.dto.MemberCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping
    public ResponseEntity createMember(MemberCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        URI uri = linkTo(MemberMeController.class).toUri();
        EntityModel<MemberCreateResponse> entityModel = new EntityModel<>(this.memberCommandService.createMember(request));
        entityModel.add(new Link(Util.DOCS_URL).withRel("profile"));
        return ResponseEntity.created(uri).body(entityModel);
    }
}
