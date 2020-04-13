package com.yunseong.second_project.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.second_project.category.command.domain.Category;
import com.yunseong.second_project.category.command.domain.CategoryRepository;
import com.yunseong.second_project.common.config.jwt.JwtTokenProvider;
import com.yunseong.second_project.member.command.application.MemberCommandService;
import com.yunseong.second_project.member.command.application.dto.MemberCreateRequest;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.query.dto.MemberSignInRequest;
import com.yunseong.second_project.product.commend.application.ProductCommandService;
import com.yunseong.second_project.product.commend.application.dto.ProductCreateRequest;
import com.yunseong.second_project.product.commend.domain.Product;
import com.yunseong.second_project.product.commend.domain.ProductRepository;
import com.yunseong.second_project.product.commend.domain.ProductType;
import com.yunseong.second_project.product.commend.domain.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    protected final String username = "Username";
    protected final String password = "Password";

    protected String getJwtToken() throws Exception {
        register();
        MemberSignInRequest request = new MemberSignInRequest(this.username, this.password);

        ResultActions perform = this.mockMvc.perform(post("/v1/members/signin")
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(request)));

        String token = perform.andReturn().getResponse().getHeader("X-AUTH-TOKEN");
        SecurityContextHolder.getContext().setAuthentication(this.jwtTokenProvider.getAuthentication(token));
        return token;
    }

    private void register() {
        try {
            this.memberRepository.findMemberByUsername(this.username).get();
        } catch (Exception e) {
            MemberCreateRequest request = new MemberCreateRequest(this.username, this.password, this.username);
            this.memberCommandService.createMember(request);
            this.memberCommandService.changeGrade(this.username, Integer.MAX_VALUE);
            return;
        }
    }

    protected Category createCategory(String name, Category parent) {
        Category category = new Category(name, parent);
        this.categoryRepository.save(category);
        return category;
    }

    protected Product createProduct(String name, String description, int value, List<Category> categories) {
        Product product = new Product(name, description, value,
                categories.stream().map(this::getType).collect(Collectors.toList()));
        this.productRepository.save(product);
        return product;
    }

    private ProductType getType(Category category) {
        Type type;
        if (category.getParent() != null) {
            type = new Type(category.getId(), category.getCategoryName(), category.getParent().getId(), category.getParent().getCategoryName());
        } else {
            type = new Type(category.getId(), category.getCategoryName());
        }
        return new ProductType(type);
    }
}
