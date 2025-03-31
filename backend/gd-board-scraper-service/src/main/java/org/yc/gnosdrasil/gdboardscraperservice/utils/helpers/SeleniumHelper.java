package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.LocatorType;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.AttributeSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Helper class for Selenium WebDriver operations with improved error handling and safety.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SeleniumHelper {

    private final WebDriverManager webDriverManager;

    /**
     * Default value to return when element text or attribute is not found
     */
    private static final String DEFAULT_NOT_FOUND_VALUE = "N/A";
    private static final int DEFAULT_WAIT_DURATION_IN_SECONDS = 30;

    public RemoteWebDriver getDriver() {
        return webDriverManager.getDriver();
    }

    public void clickElement(WebElement jobElement) {
        try {
            if (jobElement != null && jobElement.isDisplayed() && jobElement.isEnabled()) {
                jobElement.click();
                log.debug("Clicked on element: {}", jobElement);
            } else {
                log.warn("Element is not clickable: {}", jobElement);
            }
        } catch (Exception e) {
            log.error("Error clicking element: {}", e.getMessage(), e);
        }
    }

    /**
     * Find an element using the specified selector
     */
    public Optional<WebElement> findElement(WebElement parentElement, ElementLocator elementLocator) {
        return findElements(parentElement, elementLocator).stream().findFirst();
    }

    public List<WebElement> findElements(WebElement parentElement, ElementLocator elementLocator) {
        return tryFindElements(parentElement, elementLocator.locatorType(), elementLocator.locatorString());
    }

    private List<WebElement> tryFindElements(WebElement parentElement, LocatorType locatorType, String locatorString) {
        try {
            return switch (locatorType) {
                case CSS -> findElementsByCssSelector(parentElement, locatorString);
                case XPATH -> findElementsByXPath(parentElement, locatorString);
                case CLASS_NAME -> findElementsByClassName(parentElement, locatorString);
                case TAG_NAME -> findElementsByTagName(parentElement, locatorString);
                case ID -> findElementsById(parentElement, locatorString);
            };
        } catch (Exception e) {
            log.debug("Could not find elements with {} elementLocator: {}", locatorType, locatorString, e);
            return Collections.emptyList();
        }
    }

    public void waitForElement(ElementLocator elementLocator) {
        switch (elementLocator.locatorType()) {
            case CSS -> waitUntilElementLoadsByCssSelector(elementLocator.locatorString());
            case XPATH -> waitUntilElementLoadsByXPath(elementLocator.locatorString());
            case CLASS_NAME -> waitUntilElementLoadsByClassName(elementLocator.locatorString());
            case TAG_NAME -> waitUntilElementLoadsByTagName(elementLocator.locatorString());
            case ID -> waitUntilElementLoadsById(elementLocator.locatorString());
        }
    }

    /**
     * Extract a value using the specified extraction type
     */
    public String extractValueByType(WebElement element, AttributeSelector attributeSelector) {
        System.out.println("Extracting value for attribute " + attributeSelector + " with text: " + element.getText());
        switch (attributeSelector.attributeType()) {
            case ATTRIBUTE:
                return getAttribute(element, attributeSelector.attributeName(), DEFAULT_NOT_FOUND_VALUE);
            case HREF:
                return getAttribute(element, "href", DEFAULT_NOT_FOUND_VALUE);
            case INNER_HTML:
//                return getAttribute(element, "innerHTML", DEFAULT_NOT_FOUND_VALUE);
                return (String) getDriver().executeScript("return arguments[0].innerHTML;", element);
            case COMPUTED_STYLE:
                try {
                    String js = "return window.getComputedStyle(arguments[0]).getPropertyValue('" +
                            attributeSelector.attributeName() + "');";
                    return (String) ((JavascriptExecutor)
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

    public Optional<WebElement> findElementByXPath(WebElement parentElement, String xpathSelector) {
        return findElementsByXPath(parentElement, xpathSelector).stream().findFirst();
    }

    /**
     * Find elements by id
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param id The id to search for
     * @return List of found elements or empty list if none found
     */
    public List<WebElement> findElementsById(WebElement parentElement, String id) {
        try {
            log.debug("Looking up elements with id: {}", id);
            if (parentElement != null) {
                return parentElement.findElements(By.id(id));
            } else {
                return getDriver().findElements(By.id(id));
            }
        } catch (Exception e) {
            log.debug("Could not find elements with id: {}", id, e);
            return Collections.emptyList();
        }
    }

    public Optional<WebElement> findElementById(WebElement parentElement, String id) {
        return findElementsById(parentElement, id).stream().findFirst();
    }

    /**
     * Find elements by tag name
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param tagName The tag name to search for
     * @return List of found elements or empty list if none found
     */
    public List<WebElement> findElementsByTagName(WebElement parentElement, String tagName) {
        try {
            log.debug("Looking up elements with tag name: {}", tagName);
            if (parentElement != null) {
                return parentElement.findElements(By.tagName(tagName));
            } else {
                return getDriver().findElements(By.tagName(tagName));
            }
        } catch (Exception e) {
            log.debug("Could not find elements with tag name: {}", tagName, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find a single element by tag name
     *
     * @param parentElement The parent element to search within, or null to search from root
     * @param tagName The tag name to search for
     * @return Optional containing the element if found, empty Optional otherwise
     */
    public Optional<WebElement> findElementByTagName(WebElement parentElement, String tagName) {
        return findElementsByTagName(parentElement, tagName).stream().findFirst();
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
            log.info("Cannot get attribute {} from null element", attribute);
            return defaultValue;
        }

        try {
            log.info("Extracting attribute {} from element", attribute);
            String value = element.getDomAttribute(attribute);
            log.info("Attribute {} value: {}", attribute, value);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            log.info("Could not find attribute {} in element", attribute, e);
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
     * @param xpathSelector The XPath elementLocator to use
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

    /**
     * Wait for an element to be present by ID
     *
     * @param id The ID elementLocator to use
     * @param waitDurationInSeconds Maximum time to wait in seconds
     */
    public void waitUntilElementLoadsById(String id, int waitDurationInSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(waitDurationInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public void waitUntilElementLoadsById(String id) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_DURATION_IN_SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public void waitUntilElementLoadsByCssSelector(String cssSelector, int waitDurationInSeconds) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(waitDurationInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
    }

    public void waitUntilElementLoadsByCssSelector(String cssSelector) {
        log.info("Waiting for element to load by CSS selector: {}", cssSelector);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_DURATION_IN_SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
        log.info("Element loaded by CSS selector: {}", cssSelector);
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

    public void waitUntilElementLoadsByTagName(String tagName, int waitDurationInSeconds) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(waitDurationInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName(tagName)));
    }

    public void waitUntilElementLoadsByTagName(String tagName) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_DURATION_IN_SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName(tagName)));
    }

    public void goToPage(String url) {
        if (url.isBlank()) throw new RuntimeException("Can't go to the page, provided url is empty or null");
        log.info("Navigating to page: {}", url);
        getDriver().get(url);
        log.info("Current page title {}", getDriver().getTitle());
    }

    public void releaseDriver() {
        webDriverManager.releaseDriver();
    }
}