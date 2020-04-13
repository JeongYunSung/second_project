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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends BaseTest {

    @Autowired
    private OrderCommendService orderCommendService;

    @Test
    public void 주문_결제() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        this.memberCommandService.addMoney(this.username, 10000);
        Category category = this.createCategory("category5", null);
        List<Long> list = new ArrayList<>();
        IntStream.range(10, 13).forEach(i -> {
            Product product = this.createProduct("title : " + i, "description : " + i, 1000, Arrays.asList(category));
            list.add(product.getId());
        });
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        Long order = this.orderCommendService.createOrder(orderCreateRequest);
        //when
        ResultActions perform = this.mockMvc.perform(post("/v1/orders/{id}/payment", order)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("order-payment-create",
                        links(
                                linkWithRel("profile").description("관련 주소")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주문 번호")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 자원 uri")
                        ),
                        responseFields(
                                subsectionWithPath("itemsName").description("결제한 상품 제목"),
                                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총 가격"),
                                subsectionWithPath("_links").description("관련 주소")
                        )));
    }

/*    @Test
    public void 주문_결제_후_정보창() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category", null);
        List<Long> list = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        list.stream().forEach(i -> this.createProduct("title : " + i, "description : " + i, 10000, Arrays.asList(category)));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        Long order = this.orderCommendService.createOrder(orderCreateRequest);
        this.orderCommendService.completePayment(order);
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/members/me")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk());
    }*/
}