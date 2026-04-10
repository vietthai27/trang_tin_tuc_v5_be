package com.thai27.trang_tin_tuc_v5_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class TrangtintucV5BeApplication {

    static void main(String[] args) {
        SpringApplication.run(TrangtintucV5BeApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passencode() {
        return new BCryptPasswordEncoder();
    }

}
