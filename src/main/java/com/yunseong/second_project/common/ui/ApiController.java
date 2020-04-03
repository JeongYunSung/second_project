package com.yunseong.second_project.common.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/v1/docs")
public class ApiController {

    @GetMapping
    public String docs() {
        return "/docs/index.html";
    }
}
