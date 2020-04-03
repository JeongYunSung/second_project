package com.yunseong.second_project.member.command.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.member.command.application.dto.*;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.command.domain.Purchase;
import com.yunseong.second_project.product.command.domain.Product;
import com.yunseong.second_project.product.command.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberCreateResponse createMember(MemberCreateRequest request) {
        Member member = new Member(request.getUsername(), request.getPassword(), request.getNickname());
        member.encodePassword(this.passwordEncoder);
        this.memberRepository.save(member);
        return new MemberCreateResponse(member);
    }

    public MemberUpdateResponse updatePutMember(MemberUpdateRequest request) {
        Member member = getMember();
        member.verifyPassword(request.getVerifyPassword(), this.passwordEncoder);
        member.update(request.getPassword(), request.getNickname(), request.getMoney(),
                getPurchases(request));
        return new MemberUpdateResponse(member)
                .withPassword(member.getPassword()).withNickname(member.getNickname()).withMoney(member.getMoney()).withGrade(member.getGrade().getValue()).withPurchaseIdList(request.getPurchaseIdList());
    }

    public MemberUpdateResponse updatePatchMember(MemberUpdateRequest request) {
        Member member = getMember();
        member.verifyPassword(request.getVerifyPassword(), this.passwordEncoder);
        MemberUpdateResponse response = new MemberUpdateResponse(member);
        if (hasText(request.getPassword())) {
            member.changePassword(request.getPassword());
            member.encodePassword(this.passwordEncoder);
            response.withPassword(member.getPassword());
        }
        if (hasText(request.getNickname())) {
            member.changeNickname(request.getNickname());
            response.withNickname(member.getNickname());
        }
        if (request.getMoney() != null) {
            member.addMoney(request.getMoney());
            response.withMoney(member.getMoney());
        }
        if (request.getGrade() != null) {
            member.changeGrade(request.getGrade());
            response.withGrade(member.getGrade().getValue());
        }
        if (request.getPurchaseIdList() != null) {
            getPurchases(request)
            .forEach(member::addPurchase);
            response.withPurchaseIdList(request.getPurchaseIdList());
        }
        return response;
    }

    public void deleteMember() {
        Member member = getMember();
        member.delete();
    }

    private Member getMember() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.memberRepository.findById(user.getId()).orElseThrow(() -> new NotFoundEntityException("해당 유저는 존재하지 않습니다.", user.getId()));
    }

    private List<Purchase> getPurchases(MemberUpdateRequest request) {
        return request.getPurchaseIdList().stream().map(productId -> {
            Product product = this.productRepository.findById(productId).get();
            return new Purchase(productId, product.getProductName());
        }).collect(Collectors.toList());
    }
}
