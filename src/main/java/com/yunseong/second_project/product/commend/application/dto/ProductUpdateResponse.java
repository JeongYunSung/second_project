package com.yunseong.second_project.product.commend.application.dto;

import com.yunseong.second_project.product.query.application.dto.TypeResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ProductUpdateResponse {

    private Long id;
    private @With String productName;
    private @With String description;
    private @With int value;
    private @With List<TypeResponse> types;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public ProductUpdateResponse(Long id, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
}
