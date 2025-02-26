package com.study.dev.rate_limiting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String testRateLimiting() {
        return "Request Successful";
    }
}
