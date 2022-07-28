package com.example.springsecurity.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/greeting") // secured된 통신으로 받아옴
    public String greeting(){
        return "hello";
    }

    @PostMapping("/greeting") // secured된 통신으로 받아옴
    public String greeting(@RequestBody String name){
        return "hello "+name;
    }

}
