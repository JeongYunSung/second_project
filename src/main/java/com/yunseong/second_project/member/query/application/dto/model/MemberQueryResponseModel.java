package com.yunseong.second_project.member.query.application.dto.model;

import com.yunseong.second_project.member.query.application.dto.MemberQueryResponse;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

@Getter
public class MemberQueryResponseModel extends EntityModel<MemberQueryResponse> {

    private List<PurchaseResponseModel> products;

    public MemberQueryResponseModel(MemberQueryResponse content, List<PurchaseResponseModel> products, Link... links) {
        super(content, links);
        this.products = products;
    }
}
