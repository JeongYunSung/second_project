package com.yunseong.second_project.member.query;

import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.query.dto.MemberQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryResponse findMember(Long id) {
        MemberQueryResponse member = this.memberRepository.findFetchById(id).orElseThrow(() -> new NotFoundEntityException("해당 유저는 존재하지 않습니다.", id));
        return member;
    }
}
