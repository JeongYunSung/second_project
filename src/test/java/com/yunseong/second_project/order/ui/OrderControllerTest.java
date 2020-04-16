package com.yunseong.second_project.order.ui;

import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.common.BaseTest;
import com.yunseong.second_project.order.commend.application.OrderCommendService;
import com.yunseong.second_project.order.commend.application.dto.OrderCreateRequest;
import com.yunseong.second_project.product.commend.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends BaseTest {

    @Autowired
    private OrderCommendService orderCommendService;

    @Test
    public void 주문_생성() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category1", null);
        List<Long> list = new ArrayList<>();
        IntStream.range(1, 6).forEach(i -> {
            Product product = this.createProduct("title : " + i, "description : " + i, 10000, Arrays.asList(category));
            list.add(product.getId());
        });
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        //when
        ResultActions perform = this.mockMvc.perform(post("/v1/orders")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(orderCreateRequest)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("order-create",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 자원 uri")
                        )));
    }

    @Test
    public void 주문_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category1", null);
        List<Long> list = new ArrayList<>();
        IntStream.range(1, 6).forEach(i -> {
            Product product = this.createProduct("title : " + i, "description : " + i, 10000, Arrays.asList(category));
            list.add(product.getId());
        });
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        Long order = this.orderCommendService.createOrder(orderCreateRequest);
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/orders/{id}", order)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-select",
                        links(
                                linkWithRel("list").description("현재 주문 목록"),
                                linkWithRel("profile").description("관련 주소")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주문 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 번호"),
                                subsectionWithPath("orderProductResponses").description("주문 대상 상품 목록"),
                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총 가격"),
                                fieldWithPath("paymentStatus").type(JsonFieldType.STRING).description("결제 상태"),
                                subsectionWithPath("_links").description("관련 주소")
                        )));
    }

    @Test
    public void 주문_취소() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category3", null);
        List<Long> list = new ArrayList<>();
        IntStream.range(1, 6).forEach(i -> {
            Product product = this.createProduct("title : " + i, "description : " + i, 10000, Arrays.asList(category));
            list.add(product.getId());
        });
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        Long order = this.orderCommendService.createOrder(orderCreateRequest);
        //when
        ResultActions perform = this.mockMvc.perform(put("/v1/orders/{id}", order)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("order-delete",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주문 번호")
                        )));
    }
}