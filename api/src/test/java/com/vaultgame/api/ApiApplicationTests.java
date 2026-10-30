package com.vaultgame.api;

import com.vaultgame.api.config.ApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class ApiApplicationTests {

    @Test
    void contextLoads() {}
}
