package com.nhn.ep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody // = @RestController
    public String hello(){
//        return "hello world";

        System.out.println();
        return "index";
    }
}
