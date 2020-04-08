package com.yunseong.second_project.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.query.dto.MemberSignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@Import(DocumentConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class BaseTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MemberCommandService memberCommandService;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    protected final String username = "Username";
    protected final String password = "Password";

    protected String getJwtToken() throws Exception {
        register();
        MemberSignInRequest request = new MemberSignInRequest(this.username, this.password);

        ResultActions perform = this.mockMvc.perform(post("/v1/members/signin")
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request)));

        String token = perform.andReturn().getResponse().getHeader("X-AUTH-TOKEN");
        return token;
    }

    private void register() {
        try {
            this.memberRepository.findMemberByUsername(this.username);
        } catch (Exception e) {
            return;
        }
        MemberCreateRequest request = new MemberCreateRequest(this.username, this.password, this.username);
        this.memberCommandService.createMember(request);
        this.memberCommandService.changeGrade(this.username, Integer.MAX_VALUE);
    }

    protected Category createCategory(String name, Category parent) {
        Category category = new Category(name, parent);
        this.categoryRepository.save(category);
        return category;
    }
}
