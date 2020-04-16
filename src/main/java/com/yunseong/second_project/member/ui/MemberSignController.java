package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.config.jwt.JwtTokenProvider;
import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.MemberDetailsService;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.command.application.dto.MemberCreateResponse;
import com.yunseong.second_project.member.query.dto.MemberSignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/members", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class MemberSignController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;
    private final MemberCommandService memberCommandService;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody MemberSignInRequest request, Errors errors) {
        CustomUser user = (CustomUser) this.memberDetailsService.loadUserByUsername(request.getUsername());
        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            errors.rejectValue("password", "wrongValue", "Password is wrongValue");
        }
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        RepresentationModel model = new RepresentationModel();
        model.add(linkTo(MemberMeController.class).withSelfRel());
        model.add(Util.profile);
        return ResponseEntity.ok().header("X-AUTH-TOKEN", this.jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))).body(model);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody  MemberCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        URI uri = linkTo(methodOn(MemberSignController.class).signUp(request, errors)).toUri();
        EntityModel<MemberCreateResponse> entityModel = new EntityModel<>(this.memberCommandService.createMember(request));
        entityModel.add(Util.profile);
        return ResponseEntity.created(uri).body(entityModel);
    }
}
