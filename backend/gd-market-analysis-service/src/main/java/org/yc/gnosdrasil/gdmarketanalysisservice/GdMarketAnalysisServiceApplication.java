package org.yc.gnosdrasil.gdmarketanalysisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GdMarketAnalysisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdMarketAnalysisServiceApplication.class, args);
    }

}
