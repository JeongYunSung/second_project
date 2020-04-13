package com.yunseong.second_project.cart.ui;

import com.yunseong.second_project.cart.application.CartService;
import com.yunseong.second_project.cart.domain.CartItem;
import com.yunseong.second_project.cart.repository.CartRepository;
import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.common.BaseTest;
import com.yunseong.second_project.product.commend.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest extends BaseTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    
    @Test
    public void 아이템_추가() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category1", null);
        Product product = this.createProduct("product1", "product1", 100, Arrays.asList(category));
        CartItem cartItem = new CartItem(product.getId(), product.getProductName(), product.getDescription(), product.getValue());
        //when
        ResultActions perform = this.mockMvc.perform(put("/v1/carts/items/new")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken)
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(cartItem)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("cart-add-item",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        requestFields(
                                fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("상품 설명"),
                                fieldWithPath("value").type(JsonFieldType.NUMBER).description("상품 아이디")
                        )));
    }

    @Test
    public void 아이템_삭제() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category2", null);
        Product product = this.createProduct("product2", "product2", 100, Arrays.asList(category));
        CartItem cartItem = new CartItem(product.getId(), product.getProductName(), product.getDescription(), product.getValue());
        //when
        this.cartRepository.addItem(this.username, cartItem);
        ResultActions perform = this.mockMvc.perform(put("/v1/carts/items/{id}", cartItem.getProductId())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cart-delete-item",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        pathParameters(
                                parameterWithName("id").description("상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.BOOLEAN).description("삭제 여부")
                        )));
    }

    @Test
    public void 카트_비우기() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category3", null);
        Product product = this.createProduct("product3", "product3", 100, Arrays.asList(category));
        CartItem cartItem = new CartItem(product.getId(), product.getProductName(), product.getDescription(), product.getValue());
        //when
        this.cartRepository.addItem(this.username, cartItem);
        ResultActions perform = this.mockMvc.perform(delete("/v1/carts")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("cart-clean",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        )));
    }

    @Test
    public void 카트_아이템_생성_후_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        Category category = this.createCategory("category4", null);
        for(int i = 0;i<5;i++) {
            Product product = this.createProduct("product : " + i, "product : " + i, 100*i, Arrays.asList(category));
            CartItem cartItem = new CartItem(product.getId(), product.getProductName(), product.getDescription(), product.getValue());
            this.cartService.addItem(this.username, cartItem);
        }
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/carts")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cart-list-select",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("유저 아이디"),
                                subsectionWithPath("cartItems").description("상품 목록"),
                                fieldWithPath("totalMoney").type(JsonFieldType.NUMBER).description("총 금액")
                        )));
    }

/*    @Test
    public void 카트_조회() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/carts")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-AUTH-TOKEN", jwtToken));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cart-list-select",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("유저 아이디"),
                                subsectionWithPath("cartItems").description("상품 목록"),
                                fieldWithPath("totalMoney").type(JsonFieldType.NUMBER).description("총 금액")
                        )));
    }*/
}