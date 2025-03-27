package org.yc.gnosdrasil.gdboardscraperservice.factories.driver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebDriverManager {

    private final WebDriverFactory webDriverFactory;
    private final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Get a WebDriver instance for the current thread.
     *
     * @return A valid RemoteWebDriver instance
     */
    public RemoteWebDriver getDriver() {
        log.info("Getting WebDriver instance for thread: {}", Thread.currentThread().getName());
        RemoteWebDriver driver = driverThreadLocal.get();
        if (driver == null || !webDriverFactory.isDriverSessionValid(driver)) {
            if (driver != null) {
                webDriverFactory.releaseDriver(driver); // Release the old driver if it exists
            }
            log.info("WebDriver instance not found for thread: {}", Thread.currentThread().getName());
            driver = webDriverFactory.getDriver(); // Get a new driver from the factory
            log.info("Created new WebDriver instance for thread: {}", Thread.currentThread().getName());
            driverThreadLocal.set(driver); // Store the new driver in ThreadLocal
        }
        return driver;
    }

    /**
     * Return the WebDriver instance to the pool.
     */
    public void releaseDriver() {
        RemoteWebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            webDriverFactory.releaseDriver(driver); // Release the driver back to the factory
            driverThreadLocal.remove(); // Clear the ThreadLocal reference
        }
    }
}