package com.yunseong.second_project.member.command.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.exception.NotFoundUsernameException;
import com.yunseong.second_project.common.errors.exception.NotMatchPasswordException;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.command.application.dto.MemberUpdateRequest;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.command.domain.Purchase;
import com.yunseong.second_project.product.command.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(MemberCreateRequest request) {
        Member member = new Member(request.getUsername(), request.getPassword(), request.getNickname());
        this.memberRepository.save(member);
        return member.getId();
    }

    public Long updatePutMember(MemberUpdateRequest request) {
        Member member = getMember();
        verifyPassword(request, member);
        member.update(request.getPassword(), request.getNickname(), request.getMoney(),
                request.getPurchaseIdList().stream().map(productId -> new Purchase(this.productRepository.findById(productId).get())).collect(Collectors.toList()));
        return member.getId();
    }

    public Long updateFetchMember(MemberUpdateRequest request) {
        Member member = getMember();
        verifyPassword(request, member);
        if (hasText(request.getPassword())) {
            member.changePassword(request.getPassword());
        }
        if (hasText(request.getNickname())) {
            member.changeNickname(request.getNickname());
        }
        if (request.getMoney() != null) {
            member.changeMoney(request.getMoney());
        }
        if (request.getPurchaseIdList() != null) {
            request.getPurchaseIdList().stream().map(productId -> new Purchase(this.productRepository.findById(productId).get())).collect(Collectors.toList())
            .forEach(member::addPurchase);
        }
        return member.getId();
    }

    public Long deleteMember() {
        Member member = getMember();
        member.delete();
        return member.getId();
    }

    private void verifyPassword(MemberUpdateRequest request, Member member) {
        if(!member.verifyPassword(request.getVerifyPassword(), this.passwordEncoder)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    private Member getMember() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.memberRepository.findById(user.getId()).orElseThrow(() -> new NotFoundUsernameException("해당 유저이름은 존재하지 않습니다."));
    }
}
