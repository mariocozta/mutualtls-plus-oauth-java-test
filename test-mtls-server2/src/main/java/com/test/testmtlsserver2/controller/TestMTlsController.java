package com.test.testmtlsserver2.controller;


import com.test.testmtlsserver2.service.TestConnectionWithTLSService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/test")
public class TestMTlsController {

    private TestConnectionWithTLSService testConnectionWithTLSService;

    public TestMTlsController (TestConnectionWithTLSService testConnectionWithTLSService){
        this.testConnectionWithTLSService = testConnectionWithTLSService;
    }

    @GetMapping("/testSendMutualTls")
    public String testSendMutualTls() throws Exception {
        return testConnectionWithTLSService.testSendRequestWithTLS();
    }

}
