package org.yc.gnosdrasil.gdboardscraperservice.factories.driver;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@Slf4j
public class WebDriverFactory {

    @Value("${selenium.grid.url}")
    private String seleniumGridUrl;

    @Value("${selenium.pool.size}") // Default pool size is 5
    private int poolSize;

    private final BlockingQueue<RemoteWebDriver> driverPool = new ArrayBlockingQueue<>(5);

    @PostConstruct
    private void initializeWebDriverFactory() {
        log.info("Initializing WebDriverFactory with pool size: {}", poolSize);
        initializeDriverPool();
    }

    private void initializeDriverPool() {
        for (int i = 0; i < poolSize; i++) {
            log.info("Initializing WebDriver instance {}: {}", i, driverPool.offer(createNewDriver()));
        }
    }

    /**
     * Get a WebDriver instance from the pool.
     *
     * @return A valid RemoteWebDriver instance
     */
    public RemoteWebDriver getDriver() {
        try {
            RemoteWebDriver driver = driverPool.take(); // Blocks if no driver is available
            if (!isDriverSessionValid(driver)) {
                safelyQuitDriver(driver);
                driver = createNewDriver(); // Create a new driver if the session is invalid
            }
            return driver;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to get WebDriver from pool", e);
            throw new RuntimeException("Failed to get WebDriver from pool", e);
        }
    }

    /**
     * Return a WebDriver instance to the pool.
     *
     * @param driver The WebDriver instance to return
     */
    public void releaseDriver(RemoteWebDriver driver) {
        if (driver != null) {
            if (isDriverSessionValid(driver)) {
                if (!driverPool.offer(driver)) { // If the pool is full, quit the driver
                    safelyQuitDriver(driver);
                }
            } else {
                safelyQuitDriver(driver); // Quit if the session is invalid
            }
        }
    }

    /**
     * Create a new RemoteWebDriver instance
     */
    private RemoteWebDriver createNewDriver() {
        try {
            log.info("Creating new WebDriver instance");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");

            RemoteWebDriver driver = new RemoteWebDriver(new URL(seleniumGridUrl), options);
            log.info("Created new WebDriver instance: {}", driver.getSessionId());

            return driver;
        } catch (Exception e) {
            log.error("Failed to create WebDriver", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    /**
     * Check if the driver has a valid session
     */
    boolean isDriverSessionValid(RemoteWebDriver driver) { // Changed to package-private
        try {
            SessionId sessionId = driver.getSessionId();
            if (sessionId == null) {
                return false;
            }

            // Try a simple operation to verify the session is still valid
            driver.getCurrentUrl();
            return true;
        } catch (Exception e) {
            log.debug("WebDriver session is no longer valid", e);
            return false;
        }
    }

    /**
     * Safely quit a driver instance
     */
    private void safelyQuitDriver(RemoteWebDriver driver) {
        try {
            log.debug("Quitting WebDriver instance");
            driver.quit();
        } catch (Exception e) {
            log.debug("Error quitting WebDriver", e);
        }
    }

    /**
     * Clean up all WebDriver instances when the application shuts down
     */
    @PreDestroy
    public void cleanUp() {
        log.info("Cleaning up WebDriver resources");
        while (!driverPool.isEmpty()) {
            RemoteWebDriver driver = driverPool.poll();
            if (driver != null) {
                safelyQuitDriver(driver);
            }
        }
    }
}