package org.yc.gnosdrasil.gduserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GdUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdUserServiceApplication.class, args);
    }

}
