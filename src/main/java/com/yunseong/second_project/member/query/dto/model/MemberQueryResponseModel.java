package com.yunseong.second_project.member.query.dto.model;

import com.yunseong.second_project.member.query.dto.MemberQueryResponse;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

@Getter
public class MemberQueryResponseModel extends EntityModel<MemberQueryResponse> {

    public MemberQueryResponseModel(MemberQueryResponse content, Link... links) {
        super(content, links);
    }
}
