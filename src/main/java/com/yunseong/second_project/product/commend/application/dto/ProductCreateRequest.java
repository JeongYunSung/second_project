package com.yunseong.second_project.product.commend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String productName;
    @NotBlank
    @Size(min = 10, max = 500)
    private String description;
    @NotNull
    @Min(0) @Max(Integer.MAX_VALUE)
    private Integer value;
    @NotNull
    @Size(min = 1)
    private List<Long> types;
}
