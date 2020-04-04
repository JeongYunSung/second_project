package com.yunseong.second_project.category.ui;

import com.yunseong.second_project.category.command.application.CategoryCommandService;
import com.yunseong.second_project.category.query.CategoryQueryService;
import com.yunseong.second_project.category.query.CategoryResponse;
import com.yunseong.second_project.category.query.CategoryResponseModel;
import com.yunseong.second_project.common.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/categories", consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping("/{id}")
    public ResponseEntity findCategory(@PathVariable Long id) {
        Util.getUtil().validId(id);
        EntityModel<CategoryResponse> entityModel = new EntityModel<>(this.categoryQueryService.findCategory(id));
        entityModel.add(Util.profile);

        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    public ResponseEntity findCategories(@PageableDefault Pageable pageable) {
        Page<CategoryResponseModel> page = this.categoryQueryService.findCategoryByPage(pageable).map(categoryResponse -> new CategoryResponseModel(categoryResponse,
                linkTo(methodOn(CategoryController.class).findCategory(categoryResponse.getId())).withRel("child")));
        PagedModel<CategoryResponseModel> pagedModel = new PagedModel<>(page.getContent(),
                new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), page.getTotalElements()));
        pagedModel.add(Util.profile);
        return ResponseEntity.ok(pagedModel);
    }
}
