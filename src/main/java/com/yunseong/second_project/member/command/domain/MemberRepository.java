package com.yunseong.second_project.member.command.domain;

import com.yunseong.second_project.member.query.dto.MemberQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.id = :id and m.delete = false")
    Optional<Member> findById(Long id);

    @Query("select m from Member m left join fetch m.purchases p left join fetch p.purchase pur where m.id = :id and m.delete = false")
    Optional<Member> findPurchaseById(Long id);

    @Query("select m from Member m where m.username = :username and m.delete = false")
    Optional<Member> findMemberByUsername(String username);

    @Query("select distinct new com.yunseong.second_project.member.query.dto.MemberQueryResponse(m) from Member m left join m.purchases mp left join mp.purchase pc where m.id = :id and m.delete = false")
    Optional<MemberQueryResponse> findFetchById(Long id);
}
