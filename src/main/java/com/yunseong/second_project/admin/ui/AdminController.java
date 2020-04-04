package com.yunseong.second_project.admin.ui;

import com.yunseong.second_project.category.command.application.CategoryCommandService;
import com.yunseong.second_project.category.command.application.dto.CategoryCreateRequest;
import com.yunseong.second_project.category.command.application.dto.CategoryCreateResponse;
import com.yunseong.second_project.category.command.application.dto.CategoryUpdateRequest;
import com.yunseong.second_project.category.ui.CategoryController;
import com.yunseong.second_project.common.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/admin", consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {

    private final CategoryCommandService categoryCommandService;

    @PostMapping("/categories")
    public ResponseEntity registerCategory(@RequestBody CategoryCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        CategoryCreateResponse response = this.categoryCommandService.registerCategory(request);
        URI uri = linkTo(methodOn(CategoryController.class).findCategory(response.getId())).toUri();
        EntityModel<CategoryCreateResponse> entityModel = new EntityModel<>(response);
        entityModel.add(Util.profile);

        return ResponseEntity.created(uri).body(entityModel);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity updatePutCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request, Errors errors) {
        Util util = Util.getUtil();
        util.validId(id);
        return util.getUpdateResponseEntity(errors, this.categoryCommandService.updatePutCategory(id, request),
                linkTo(methodOn(CategoryController.class).findCategory(id)).withSelfRel());
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity updatePatchCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request, Errors errors) {
        Util util = Util.getUtil();
        util.validId(id);
        return util.getUpdateResponseEntity(errors, this.categoryCommandService.updatePatchCategory(id, request),
                linkTo(methodOn(CategoryController.class).findCategory(id)).withSelfRel());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        Util util = Util.getUtil();
        util.validId(id);
        this.categoryCommandService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
