package org.yc.gnosdrasil.gdroadmapservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GdRoadmapServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdRoadmapServiceApplication.class, args);
    }

}
