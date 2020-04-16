package com.yunseong.second_project.category.ui;

import com.yunseong.second_project.category.command.application.CategoryCommandService;
import com.yunseong.second_project.category.command.application.dto.CategoryCreateRequest;
import com.yunseong.second_project.category.command.application.dto.CategoryUpdateRequest;
import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends BaseTest {

    @Autowired
    private CategoryCommandService categoryCommandService;

    @Test
    @Transactional(readOnly = true)
    public void 카테고리_목록_조회_최적화_테스트() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        int pageSize = 10;
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Category> list = new ArrayList<>();
        IntStream.range(1, 11).forEach(i -> list.add(this.createCategory(i + " : Category", null)));
        long size = (list.size() / 2) + 1;
        for (int i = 0; i < size - 1; i++) {
            this.categoryCommandService.updatePutCategory(i + 2L, new CategoryUpdateRequest("test",
                    i + 1L));
            this.categoryCommandService.updatePutCategory(i + size, new CategoryUpdateRequest("test",
                    i + 1L));
        }
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/categories")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(pageable)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 카테고리_생성() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        String parentName = "Parent";
        String childName = "Child";
        Category category = this.createCategory(parentName, null);
        CategoryCreateRequest childRequest = new CategoryCreateRequest(childName, category.getId());
        //when
        ResultActions perform = this.mockMvc.perform(post("/v1/categories")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(childRequest)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("category-create",
                        links(
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        requestFields(
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 카테고리 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 자원 주소")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 카테고리 아이디"),
                                fieldWithPath("parentName").type(JsonFieldType.STRING).description("부모 카테고리 이름"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                subsectionWithPath("_links").description("카테고리생성 관련 주소")
                        )));
    }

    @Test
    public void 카테고리_삭제() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("임시카테고리", null);
        //when
        ResultActions perform = this.mockMvc.perform(delete("/v1/categories/{id}", category.getId())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("category-delete",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        )));
    }

    @Test
    @Description("아이디범위가 틀려서 실패하는 케이스")
    public void 카테고리_삭제_실패1() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        //when
        ResultActions perform = this.mockMvc.perform(delete("/v1/categories/{id}", 0L)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 카테고리_수정() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category parent = this.createCategory("Parent", null);
        Category child = this.createCategory("Child", null);
        CategoryUpdateRequest request = new CategoryUpdateRequest("updateChild", parent.getId());
        //when
        ResultActions perform = this.mockMvc.perform(put("/v1/categories/{id}", child.getId())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("category-update",
                        links(
                                linkWithRel("self").description("현재 카테고리 목록"),
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 카테고리 아이디"),
                                fieldWithPath("parentName").type(JsonFieldType.STRING).description("부모 카테고리 이름"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                fieldWithPath("updatedTime").type(JsonFieldType.STRING).description("수정 시각"),
                                subsectionWithPath("_links").description("카테고리생성 관련 주소")
                        )));
    }

    @Test
    public void 카테고리_수정_부모_삭제() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category parent = this.createCategory("Parent", null);
        Category child = this.createCategory("Child", parent);
        CategoryUpdateRequest request = new CategoryUpdateRequest("updateChild", 0L);
        //when
        ResultActions perform = this.mockMvc.perform(put("/v1/categories/{id}", child.getId())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 카테고리_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category grandParent = this.createCategory("GradParent", null);
        Category parent = this.createCategory("Parent", grandParent);
        this.createCategory("Child1", parent);
        this.createCategory("Child2", parent);
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/categories/{id}", parent.getId())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
/*                .andDo(print())*/
                .andExpect(status().isOk())
                .andDo(document("category-select",
                        links(
                                linkWithRel("self").description("현재 카테고리 목록"),
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 카테고리 아이디"),
                                fieldWithPath("parentName").type(JsonFieldType.STRING).description("부모 카테고리 이름"),
                                subsectionWithPath("child").description("자식 카테고리 정보"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                fieldWithPath("updatedTime").type(JsonFieldType.STRING).description("수정 시각"),
                                subsectionWithPath("_links").description("카테고리생성 관련 주소")
                        )));
    }

    @Test
    public void 카테고리_목록_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        int pageSize = 10;
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Category> list = new ArrayList<>();
        IntStream.range(1, 11).forEach(i -> list.add(this.createCategory(i + " : Category", null)));
        int size = (list.size()/2);
        for (int i = 0; i < size-1; i++) {
            this.categoryCommandService.updatePutCategory(list.get(i).getId(), new CategoryUpdateRequest(list.get(i).getCategoryName(),
                    list.get(i+1).getId()));
            this.categoryCommandService.updatePutCategory(list.get(i+size).getId(), new CategoryUpdateRequest(list.get(i+size).getCategoryName(),
                    list.get(i+1).getId()));
        }
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/categories")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(pageable)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("category-list-select",
                        links(
                                linkWithRel("self").description("현재 카테고리 목록"),
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        requestParameters(
                                parameterWithName("size").optional().description("페이지 크기"),
                                parameterWithName("page").optional().description("현재 페이지"),
                                parameterWithName("sort").optional().description("정렬방식")
                        ),
                        responseFields(
                                subsectionWithPath("page").description("현재 페이지 정보"),
                                subsectionWithPath("_embedded.categoryResponseList").description("카테고리 리스트 정보"),
                                subsectionWithPath("_links").description("카테고리생성 관련 주소")
                        )));
    }
}