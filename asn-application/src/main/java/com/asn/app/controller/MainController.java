package com.asn.app.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class MainController {
    
    @GetMapping("/")
    public String index() throws IOException {
        return "redirect:/swagger-ui.html";
    }
}