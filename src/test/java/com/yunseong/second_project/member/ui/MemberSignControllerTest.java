package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.BaseTest;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.query.dto.MemberSignInRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberSignControllerTest extends BaseTest {

    @Test
    public void 회원가입_테스트() throws Exception {
        //given
        String username = "testUsername";
        String nickname = "testNickname";
        String password = "testPassword";
        MemberCreateRequest request = new MemberCreateRequest(username, password, nickname);
        //when
        ResultActions perform = this.mockMvc.perform(post("/v1/members/signup")
                        .contentType(MediaTypes.HAL_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(request)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("member-signUp",
                        links(
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 별칭")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 자원 주소")
                        ),
                        responseFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 별칭"),
                                fieldWithPath("grade").type(JsonFieldType.STRING).description("유저 계급"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                subsectionWithPath("_links").description("유저생성 관련 주소")
                        )));
    }

    @Test
    public void 회원가입_중복_테스트() throws Exception {
        //given
        String username = "testReplicationUsername";
        String nickname = "testReplicationNickname";
        String password = "testReplicationPassword";
        MemberCreateRequest request = new MemberCreateRequest(username, password, nickname);
        //when
        this.memberCommandService.createMember(request);
        ResultActions perform = this.mockMvc.perform(post("/v1/members/signup")
                        .contentType(MediaTypes.HAL_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(request)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 로그인_테스트() throws Exception {
        //given
        MemberCreateRequest createRequest = new MemberCreateRequest(this.username, this.password, this.username);
        MemberSignInRequest request = new MemberSignInRequest(this.username, this.password);
        //when
        this.memberCommandService.createMember(createRequest);
        ResultActions perform = this.mockMvc.perform(post("/v1/members/signin")
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request)));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk());

        String token = perform.andReturn().getResponse().getHeader("X-AUTH-TOKEN");
        Assertions.assertNotNull(token);

        perform
                .andDo(document("member-signIn",
                        links(
                                linkWithRel("self").description("내 자원 확인"),
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseFields(
                                subsectionWithPath("_links").description("유저로그인 관련 주소")
                        )));
    }
}