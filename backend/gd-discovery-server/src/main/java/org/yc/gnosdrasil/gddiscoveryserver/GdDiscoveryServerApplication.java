package org.yc.gnosdrasil.gddiscoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GdDiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdDiscoveryServerApplication.class, args);
    }

}
