package com.test.testmtlsserver1.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping( "/testMtls")
public class TestMTlsController {


    @GetMapping("/protected")
    public String protectedEndpoint(){
        return "hello miau protected!";
    }

    @GetMapping("/notProtected")
    public String notProtected(){
        return "hello miau!";
    }



}
