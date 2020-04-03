package com.yunseong.second_project.common.util;

import com.yunseong.second_project.common.errors.NotValidIdException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

public class Util {

    public final static String SERVER_URL = "http://localhost:8080";

    public final static String DOCS_URL = SERVER_URL + "/docs/index.html";

    public final static Link profile = new Link(DOCS_URL).withRel("profile");

    private Util() {}

    public final static Util getUtil() {
        return new Util();
    }

    public <T> ResponseEntity getUpdateResponseEntity(Errors errors, T t, Link...links) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        EntityModel<T> entityModel = new EntityModel<>(t);
        entityModel.add(links);
        entityModel.add(Util.profile);

        return ResponseEntity.ok(entityModel);
    }

    public void validId(Long id) {
        if (id == null || id < 1 || id > Long.MAX_VALUE) {
            throw new NotValidIdException("해당 아이디값은 유효한 값이 아닙니다.", id);
        }
    }
}
