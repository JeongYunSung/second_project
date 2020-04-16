package com.yunseong.second_project.order.ui;

import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.common.BaseTest;
import com.yunseong.second_project.order.commend.application.OrderCommendService;
import com.yunseong.second_project.order.commend.application.dto.OrderCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderMyControllerTest extends BaseTest {

    @Autowired
    private OrderCommendService orderCommendService;

    @Test
    public void 주문_목록_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category", null);
        List<Long> list = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        list.stream().forEach(i -> this.createProduct("title : " + i, "description : " + i, 10000, Arrays.asList(category)));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(list);
        this.orderCommendService.createOrder(orderCreateRequest);
        this.orderCommendService.createOrder(orderCreateRequest);
        this.orderCommendService.createOrder(orderCreateRequest);
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/orders/my")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-list-select",
                        links(
                                linkWithRel("profile").description("관련 주소")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseFields(
                                subsectionWithPath("_embedded.orderResponseList").description("주문 정보"),
                                subsectionWithPath("_links").description("관련 주소"),
                                subsectionWithPath("page").description("해당 페이지정보")
                        )));
    }
}