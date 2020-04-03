package com.yunseong.second_project.member.command.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = this.memberRepository.findMemberByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 계정은 존재하지 않습니다."));
        member.isDelete();
        return new CustomUser(member.getId(),
                member.getUsername(),
                member.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.getGrade().getValue())));
    }
}
