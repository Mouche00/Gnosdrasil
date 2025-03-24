package org.yc.gnosdrasil.gdboardscraperservice.factories;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@Slf4j
public class WebDriverFactory {

    @Value("${selenium.grid.url}")
    private String seleniumGridUrl;

    private final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();


    /**
     * Get a WebDriver instance. Creates a new one if none exists or if the current one is invalid.
     *
     * @return A valid RemoteWebDriver instance
     */
    public RemoteWebDriver getDriver() {
        RemoteWebDriver driver = driverThreadLocal.get();

        // Check if driver exists and has a valid session
        if (driver == null || !isDriverSessionValid(driver)) {
            if (driver != null) {
                safelyQuitDriver(driver);
            }

            driver = createNewDriver();
            driverThreadLocal.set(driver);
        }

        return driver;
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

            return new RemoteWebDriver(new URL(seleniumGridUrl), options);
        } catch (Exception e) {
            log.error("Failed to create WebDriver", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    /**
     * Check if the driver has a valid session
     */
    private boolean isDriverSessionValid(RemoteWebDriver driver) {
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
     * Release the current thread's WebDriver
     */
    public void releaseDriver() {
        RemoteWebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            safelyQuitDriver(driver);
            driverThreadLocal.remove();
        }
    }

    /**
     * Clean up all WebDriver instances when the application shuts down
     */
    @PreDestroy
    public void cleanUp() {
        log.info("Cleaning up WebDriver resources");
        RemoteWebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            safelyQuitDriver(driver);
            driverThreadLocal.remove();
        }
    }
}