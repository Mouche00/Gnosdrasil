package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.yc.gnosdrasil.gdboardscraperservice.factories.WebDriverFactory;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.AttributeSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.FieldSelector;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for Selenium WebDriver operations with improved error handling and safety.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SeleniumHelper {

    private final WebDriverFactory webDriverFactory;

    /**
     * Default value to return when element text or attribute is not found
     */
    private static final String DEFAULT_NOT_FOUND_VALUE = "N/A";
    private static final int DEFAULT_WAIT_DURATION_IN_SECONDS = 30;

    public RemoteWebDriver getDriver() {
        return webDriverFactory.getDriver();
    }

    /**
     * Find an element using the specified selector
     */
    public Optional<WebElement> findElement(WebElement parentElement, ElementLocator elementLocator) {
        return switch (elementLocator.selectorType()) {
            case CSS -> findElementsByCssSelector(parentElement, elementLocator.locatorString())
                    .stream().findFirst();
            case XPATH -> findElementsByXPath(parentElement, elementLocator.locatorString())
                    .stream().findFirst();
            case CLASS_NAME -> findElementByClassName(parentElement, elementLocator.locatorString());
        };
    }



    /**
     * Extract a value using the specified extraction type
     */
    public String extractValueByType(WebElement element, AttributeSelector attributeSelector) {
        switch (attributeSelector.attributeType()) {
            case ATTRIBUTE:
                return getAttribute(element, attributeSelector.attributeName(), DEFAULT_NOT_FOUND_VALUE);
            case HREF:
                return getAttribute(element, "href", DEFAULT_NOT_FOUND_VALUE);
            case INNER_HTML:
                return getAttribute(element, "innerHTML", DEFAULT_NOT_FOUND_VALUE);
            case COMPUTED_STYLE:
                try {
                    String js = "return window.getComputedStyle(arguments[0]).getPropertyValue('" +
                            attributeSelector.attributeName() + "');";
                    return (String) ((org.openqa.selenium.JavascriptExecutor)
                            getDriver()).executeScript(js, element);
                } catch (Exception e) {
                    log.debug("Failed to get computed style", e);
                    return DEFAULT_NOT_FOUND_VALUE;
                }
            default:
                return element.getText().trim();
        }
    }


    /**
     * Find elements using XPath elementLocator
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param xpathSelector The XPath elementLocator to use
     * @return List of found elements or empty list if none found
     */
    public List<WebElement> findElementsByXPath(WebElement parentElement, String xpathSelector) {
        try {
            log.debug("Looking up elements with XPath elementLocator: {}", xpathSelector);
            if (parentElement != null) {
                return parentElement.findElements(By.xpath(xpathSelector));
            } else {
                return getDriver().findElements(By.xpath(xpathSelector));
            }
        } catch (Exception e) {
            log.debug("Could not find elements with XPath elementLocator: {}", xpathSelector, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find elements using CSS elementLocator
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param cssSelector The CSS elementLocator to use
     * @return List of found elements or empty list if none found
     */
    public List<WebElement> findElementsByCssSelector(WebElement parentElement, String cssSelector) {
        try {
            log.debug("Looking up elements with CSS elementLocator: {}", cssSelector);
            if (parentElement != null) {
                return parentElement.findElements(By.cssSelector(cssSelector));
            } else {
                return getDriver().findElements(By.cssSelector(cssSelector));
            }
        } catch (Exception e) {
            log.debug("Could not find elements with CSS elementLocator: {}", cssSelector, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find a single element by CSS elementLocator
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param className The class name to search for (partial match)
     * @return Optional containing the element if found, empty Optional otherwise
     */
    public Optional<WebElement> findElementByCssSelector(WebElement parentElement, String className) {
        List<WebElement> elements = findElementsByCssSelector(parentElement, className);
        return elements.stream().findFirst();
    }

    /**
     * Find elements by class name (partial match using XPath)
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param className The class name to search for (partial match)
     * @return List of found elements or empty list if none found
     */
    public List<WebElement> findElementsByClassName(WebElement parentElement, String className) {
        return findElementsByXPath(
                parentElement,
                String.format(".//*[contains(@class, '%s')]", className));
    }

    /**
     * Find a single element by class name (partial match)
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param className The class name to search for (partial match)
     * @return Optional containing the element if found, empty Optional otherwise
     */
    public Optional<WebElement> findElementByClassName(WebElement parentElement, String className) {
        List<WebElement> elements = findElementsByClassName(parentElement, className);
        return elements.stream().findFirst();
    }

    /**
     * Check if an element with the given elementLocator exists
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param xpathSelector The XPath elementLocator to use
     * @return true if at least one element is found, false otherwise
     */
    public boolean isElementPresent(WebElement parentElement, String xpathSelector) {
        return !findElementsByXPath(parentElement, xpathSelector).isEmpty();
    }

    /**
     * Check if an element with the given CSS elementLocator exists
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param cssSelector The CSS elementLocator to use
     * @return true if at least one element is found, false otherwise
     */
    public boolean isElementPresentByCss(WebElement parentElement, String cssSelector) {
        return !findElementsByCssSelector(parentElement, cssSelector).isEmpty();
    }

    /**
     * Safely get text from an element found by class name
     *
     * @param parentElement The parent element to search within
     * @param className The class name to search for (partial match)
     * @return The text content of the element or DEFAULT_NOT_FOUND_VALUE if not found
     */
    public String getTextByClassName(WebElement parentElement, String className) {
        return getTextByClassName(parentElement, className, DEFAULT_NOT_FOUND_VALUE);
    }

    /**
     * Safely get text from an element found by class name with custom default value
     *
     * @param parentElement The parent element to search within
     * @param className The class name to search for (partial match)
     * @param defaultValue The default value to return if element not found
     * @return The text content of the element or defaultValue if not found
     */
    public String getTextByClassName(WebElement parentElement, String className, String defaultValue) {
        try {
            return findElementByClassName(parentElement, className)
                    .map(element -> {
                        log.debug("Extracting text from element with class: {}", className);
                        return element.getText().trim();
                    })
                    .orElse(defaultValue);
        } catch (Exception e) {
            log.debug("Error getting text from element with class: {}", className, e);
            return defaultValue;
        }
    }

    /**
     * Get attribute from an element with error handling
     *
     * @param element The element to get the attribute from
     * @param attribute The attribute name
     * @return The attribute value or DEFAULT_NOT_FOUND_VALUE if not found
     */
    public String getAttribute(WebElement element, String attribute) {
        return getAttribute(element, attribute, DEFAULT_NOT_FOUND_VALUE);
    }

    /**
     * Get attribute from an element with error handling and custom default value
     *
     * @param element The element to get the attribute from
     * @param attribute The attribute name
     * @param defaultValue The default value to return if attribute not found
     * @return The attribute value or defaultValue if not found
     */
    public String getAttribute(WebElement element, String attribute, String defaultValue) {
        if (element == null) {
            log.debug("Cannot get attribute {} from null element", attribute);
            return defaultValue;
        }

        try {
            log.debug("Extracting attribute {} from element", attribute);
            String value = element.getDomAttribute(attribute);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            log.debug("Could not find attribute {} in element", attribute, e);
            return defaultValue;
        }
    }

    /**
     * Find an element by class name and get an attribute from it
     *
     * @param parentElement The parent element to search within
     * @param className The class name to search for (partial match)
     * @param attribute The attribute name to get
     * @return The attribute value or DEFAULT_NOT_FOUND_VALUE if not found
     */
    public String getAttributeByClassName(WebElement parentElement, String className, String attribute) {
        return getAttributeByClassName(parentElement, className, attribute, DEFAULT_NOT_FOUND_VALUE);
    }

    /**
     * Find an element by class name and get an attribute from it with custom default value
     *
     * @param parentElement The parent element to search within
     * @param className The class name to search for (partial match)
     * @param attribute The attribute name to get
     * @param defaultValue The default value to return if not found
     * @return The attribute value or defaultValue if not found
     */
    public String getAttributeByClassName(WebElement parentElement, String className, String attribute, String defaultValue) {
        try {
            return findElementByClassName(parentElement, className)
                    .map(element -> getAttribute(element, attribute, defaultValue))
                    .orElse(defaultValue);
        } catch (Exception e) {
            log.debug("Error getting attribute {} from element with class: {}", attribute, className, e);
            return defaultValue;
        }
    }

    /**
     * Wait for an element to be present and return it
     *
     * @param xpathSelector The CSS elementLocator to use
     * @param waitDurationInSeconds Maximum time to wait in seconds
     */
    public void waitUntilElementLoadsByXPath(String xpathSelector, int waitDurationInSeconds) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(waitDurationInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathSelector)));
    }

    public void waitUntilElementLoadsByXPath(String xpathSelector) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_DURATION_IN_SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathSelector)));
    }

    public void waitUntilElementLoadsByClassName(String className, int waitDurationInSeconds) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(waitDurationInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                        String.format(".//*[contains(@class, '%s')]", className))));
    }

    public void waitUntilElementLoadsByClassName(String className) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_DURATION_IN_SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                        String.format(".//*[contains(@class, '%s')]", className))));
    }

    public void goToPage(String url) {
        if (url.isBlank()) throw new RuntimeException("Can't go to the page, provided url is empty or null");
        getDriver().get(url);
        log.debug("Current page title {}", getDriver().getTitle());
    }

    public void releaseDriver() {
        webDriverFactory.releaseDriver();
    }
}