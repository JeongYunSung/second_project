package com.yunseong.second_project.common.util;

import com.yunseong.second_project.common.errors.NotValidIdException;
import com.yunseong.second_project.product.query.application.dto.ProductSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import static org.springframework.util.StringUtils.*;

public class Util {

    public final static String SERVER_URL = "http://localhost:8080";

    public final static String DOCS_URL = SERVER_URL + "/docs/index.html";

    public final static Link profile = new Link(DOCS_URL).withRel("profile");

    private Util() {}

    public final static Util getUtil() {
        return new Util();
    }

    public <T> ResponseEntity getUpdateResponseEntity(Errors errors, T t, Link...links) {
        if (errors != null) {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(errors);
            }
        }
        EntityModel<T> entityModel = new EntityModel<>(t);
        entityModel.add(links);
        entityModel.add(Util.profile);

        return ResponseEntity.ok(entityModel);
    }

    public String getPageableQuery(Pageable pageable) {
        String query = "";
        if (pageable != null) {
            query += "?";
            if (pageable.getPageNumber() > 0)
                query += "page=" + pageable.getPageNumber() + "&";
            if (pageable.getPageSize() > 0 && pageable.getPageSize() != 10)
                query += "size=" + pageable.getPageSize() + "&";
            if (pageable.getSort() != null && !pageable.getSort().isUnsorted())
                query += "sort=" + pageable.getSort() + "&";
            query = query.substring(0, query.length()-1);
        }
        return query;
    }

    public String getProductConditionQuery(ProductSearchCondition condition, boolean queryCheck) {
        String query = "";
        if (condition != null) {
            if(queryCheck)
                query += "?";
            if (condition.getMin() != null && !condition.getMin().equalsIgnoreCase("null") && Integer.parseInt(condition.getMin()) >= 0)
                query += "min=" + condition.getMin() + "&";
            if (condition.getMax() != null && !condition.getMax().equalsIgnoreCase("null") && Integer.parseInt(condition.getMax()) <= Integer.MAX_VALUE)
                query += "max=" + condition.getMax() + "&";
            if (hasText(condition.getCategory()))
                query += "categoryName=" + condition.getCategory() + "&";
            if (hasText(condition.getProduct()))
                query += "productName=" + condition.getProduct() + "&";
            query = query.substring(0, query.length()-1);
        }
        return query;
    }

    public void validId(Long id) {
        if (id == null || id < 1 || id > Long.MAX_VALUE) {
            throw new NotValidIdException("해당 아이디값은 유효한 값이 아닙니다.");
        }
    }
}
