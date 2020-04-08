package com.yunseong.second_project.product.ui;

import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.product.commend.application.ProductCommandService;
import com.yunseong.second_project.product.commend.application.dto.ProductCreateRequest;
import com.yunseong.second_project.product.commend.application.dto.ProductCreateResponse;
import com.yunseong.second_project.product.commend.application.dto.ProductUpdateRequest;
import com.yunseong.second_project.product.query.application.ProductQueryService;
import com.yunseong.second_project.product.query.application.dto.ProductResponse;
import com.yunseong.second_project.product.query.application.dto.ProductResponseModel;
import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import com.yunseong.second_project.product.query.application.dto.RecommendResponse;
import com.yunseong.second_project.product.ui.validator.ProductSearchConditionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/products", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;
    private final ProductSearchConditionValidator productSearchConditionValidator;

    @PostMapping("/register")
    public ResponseEntity createProduct(@RequestBody ProductCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        ProductCreateResponse product = this.productCommandService.createProduct(request);
        EntityModel<ProductCreateResponse> entityModel = new EntityModel<>(product);
        entityModel.add(Util.profile);

        URI uri = linkTo(methodOn(ProductController.class).getProduct(product.getId())).toUri();

        return ResponseEntity.created(uri).body(entityModel);
    }

    @GetMapping
    public ResponseEntity getProducts(@PageableDefault Pageable pageable) {
        PagedModel<ProductResponseModel> pagedModel = getProductResponseModels(this.productQueryService.findByPage(pageable).map(product -> new ProductResponseModel(product,
                linkTo(methodOn(ProductController.class).getProduct(product.getProductId())).withRel("product"))), pageable);
        pagedModel.add(linkTo(ProductController.class).slash("search" + Util.getUtil().getPageableQuery(pageable)).withSelfRel());
        pagedModel.add(Util.profile);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/search")
    public ResponseEntity getProducts(@PageableDefault Pageable pageable, ProductSearchCondition condition, Errors errors) {
        this.productSearchConditionValidator.validate(condition, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.ok(this.productQueryService.findByPage(pageable));
        }
        PagedModel<ProductResponseModel> pagedModel = getProductResponseModels(this.productQueryService.findPageBySearch(condition, pageable).map(product -> new ProductResponseModel(product,
                linkTo(methodOn(ProductController.class).getProduct(product.getProductId())).withRel("product"))), pageable);
        Util util = Util.getUtil();
        pagedModel.add(linkTo(ProductController.class).slash("search" + util.getPageableQuery(pageable) + util.getProductConditionQuery(condition, false)).withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }

    private PagedModel<ProductResponseModel> getProductResponseModels(Page<ProductResponseModel> page, @PageableDefault Pageable pageable) {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), page.getTotalElements());
        PagedModel<ProductResponseModel> productResponseModels = new PagedModel<>(page.getContent(), metadata);
        productResponseModels.add(Util.profile);
        return productResponseModels;
    }

    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable Long id) {
        Util util = Util.getUtil();
        util.validId(id);
        ProductResponseModel model = new ProductResponseModel(this.productQueryService.findProduct(id));
        model.add(linkTo(methodOn(ProductController.class).getProduct(id)).withSelfRel());
//        model.add(linkTo(methodOn(ProductController.class).getProducts(null)).withRel("list"));
        model.add(Util.profile);

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePutProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request, Errors errors) {
        Util util = Util.getUtil();
        util.validId(id);
        return util.getUpdateResponseEntity(errors, this.productCommandService.updatePutProduct(id, request),
                linkTo(methodOn(ProductController.class).getProduct(id)).withSelfRel());
    }

    @PutMapping("/{id}/recommend")
    public ResponseEntity updateRecommendProduct(@PathVariable Long id) {
        Util util = Util.getUtil();
        util.validId(id);
        RecommendResponse recommendResponse = this.productCommandService.updateRecommendProduct(id);
        EntityModel<RecommendResponse> entityModel = new EntityModel<>(recommendResponse);
        entityModel.add(linkTo(methodOn(ProductController.class).getProduct(id)).withSelfRel());
        entityModel.add(Util.profile);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        Util util = Util.getUtil();
        util.validId(id);
        this.productCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
