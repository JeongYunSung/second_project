package com.yunseong.second_project.member.command.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.member.command.application.dto.*;
import com.yunseong.second_project.member.command.domain.*;
import com.yunseong.second_project.member.query.dto.PurchaseResponse;
import com.yunseong.second_project.product.commend.domain.Product;
import com.yunseong.second_project.product.commend.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.Lint;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
        Member member = getMember(false);
        member.verifyPassword(request.getVerifyPassword(), this.passwordEncoder);
        member.update(request.getPassword(), request.getNickname(), request.getMoney());
        return new MemberUpdateResponse(member)
                .withPassword(member.getPassword()).withNickname(member.getNickname()).withMoney(member.getMoney()).withGrade(member.getGrade().getValue());
    }

    public boolean changeGrade(String username, int exp) {
        try {
            Member member = this.memberRepository.findMemberByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));
            member.changeGrade(exp);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }

    public void addMoney(String username, int money) {
        Member member = this.memberRepository.findMemberByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));
        member.addMoney(money);
    }

    public List<PurchaseResponse> addPurchase(List<Long> purchaseIdList) {
        List<PurchaseResponse> list = new ArrayList<>();
        Member member = getMember(true);
        purchaseIdList.stream().map(id -> {
            Product product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("해당 상품은 존재하지 않습니다.", id));
            MemberPurchase memberPurchase = new MemberPurchase(new Purchase(product.getId(), product.getProductName()));
            memberPurchase.setMember(member);
            list.add(new PurchaseResponse(memberPurchase));
            return memberPurchase;
        }).forEach(member::addPurchase);

        return list;
    }

/*    public MemberUpdateResponse updatePatchMember(MemberUpdateRequest request) {
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
    }*/

    public void deleteMember() {
        Member member = getMember(false);
        member.delete();
    }

    private Member getMember(boolean purchase) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Supplier<NotFoundEntityException> notFoundEntityExceptionSupplier = () -> new NotFoundEntityException("해당 유저는 존재하지 않습니다.", user.getId());
        if (purchase) {
            return this.memberRepository.findPurchaseById(user.getId()).orElseThrow(notFoundEntityExceptionSupplier);
        }
        return this.memberRepository.findById(user.getId()).orElseThrow(notFoundEntityExceptionSupplier);
    }
}
