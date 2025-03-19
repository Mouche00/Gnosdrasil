package org.yc.gnosdrasil.gdboardscraperservice.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.MalformedURLException;
import java.net.URI;

@Configuration
@Slf4j
public class SeleniumConfig {

    @Value("${selenium.grid.url}")
    private String seleniumGridUrl;

    @Bean
    @Scope("prototype")
    public ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return options;
    }

    @Bean
    @Scope("prototype")
    public RemoteWebDriver remoteWebDriver(ChromeOptions options) {
        try {
            return new RemoteWebDriver(
                    URI.create(seleniumGridUrl).toURL(),
                    options
            );

        } catch (MalformedURLException e) {
            log.error("Invalid Selenium Grid URL {}", e.getMessage(), e);
            throw new IllegalArgumentException("Invalid Selenium Grid URL", e);
        }
    }
}