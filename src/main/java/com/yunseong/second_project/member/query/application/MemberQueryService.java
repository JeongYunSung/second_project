package com.yunseong.second_project.member.query.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.NotFoundUserException;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.query.application.dto.MemberQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryResponse selectMember() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = this.memberRepository.findById(user.getId()).orElseThrow(() -> new NotFoundUserException("해당 유저는 존재하지 않습니다.", user.getId()));
        return new MemberQueryResponse(member);
    }
}
