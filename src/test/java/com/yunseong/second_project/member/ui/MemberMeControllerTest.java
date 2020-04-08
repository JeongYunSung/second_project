package com.yunseong.second_project.member.ui;

import com.yunseong.second_project.common.BaseTest;
import com.yunseong.second_project.member.command.application.dto.MemberUpdateRequest;
import com.yunseong.second_project.member.command.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberMeControllerTest extends BaseTest {

    @Test
    public void 정보_업데이트() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        MemberUpdateRequest request = new MemberUpdateRequest("updatePassword",
                this.password, "updateNickname", 10000);
        //when
        ResultActions perform = this.mockMvc.perform(put("/v1/members/me")
                .header("X-AUTH-TOKEN", jwtToken)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request))
                .contentType(MediaTypes.HAL_JSON_VALUE));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        links(
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 비밀번호"),
                                fieldWithPath("verifyPassword").type(JsonFieldType.STRING).description("유저 비밀번호 확인"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 별칭"),
                                fieldWithPath("money").type(JsonFieldType.NUMBER).description("유저 소유자산")
                        ),
                        responseFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 별칭"),
                                fieldWithPath("money").type(JsonFieldType.NUMBER).description("유저 소유자산"),
                                fieldWithPath("grade").type(JsonFieldType.STRING).description("유저 계급"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                fieldWithPath("updatedTime").type(JsonFieldType.STRING).description("수정 시각"),
                                subsectionWithPath("_links").description("유저생성 관련 주소")
                        )));
    }

    @Test
    public void 내정보() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        //when
        ResultActions perform = this.mockMvc.perform(get("/v1/members/me")
                .header("X-AUTH-TOKEN", jwtToken)
                .accept(MediaTypes.HAL_JSON_VALUE));
        //then
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        links(
                                linkWithRel("profile").description("API 관련 정보")
                        ),
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        ),
                        responseFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 별칭"),
                                fieldWithPath("money").type(JsonFieldType.NUMBER).description("유저 소유자산"),
                                fieldWithPath("grade").type(JsonFieldType.STRING).description("유저 계급"),
                                fieldWithPath("createdTime").type(JsonFieldType.STRING).description("생성 시각"),
                                fieldWithPath("updatedTime").type(JsonFieldType.STRING).description("수정 시각"),
                                subsectionWithPath("purchase").description("구매물품 목록"),
                                subsectionWithPath("_links").description("유저생성 관련 주소")
                        )));
    }

    @Test
    public void 유저_삭제() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        //when
        ResultActions perform = this.mockMvc.perform(delete("/v1/members/me")
                .header("X-AUTH-TOKEN", jwtToken)
                .accept(MediaTypes.HAL_JSON_VALUE));
        //then
        perform
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("member-delete",
                        requestHeaders(
                                headerWithName("X-AUTH-TOKEN").description("자원의 접근하기위한 해당 유저의 토큰값")
                        )));
    }

    @Test
    public void 유저_삭제_후_접근() throws Exception {
        //given
        String jwtToken = this.getJwtToken();
        //when
        this.mockMvc.perform(delete("/v1/members/me")
                .header("X-AUTH-TOKEN", jwtToken)
                .accept(MediaTypes.HAL_JSON_VALUE));
        Optional<Member> memberByUsername = this.memberRepository.findMemberByUsername(this.username);
        //then
        Assertions.assertThrows(NoSuchElementException.class, () -> memberByUsername.get());
    }
}