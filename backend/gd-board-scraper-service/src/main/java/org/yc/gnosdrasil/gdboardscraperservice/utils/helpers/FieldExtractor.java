package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.FieldSelector;

import java.util.Optional;

import static org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.StringHelper.applyRegexPattern;

/**
 * Extracts field values from WebElements using different strategies
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FieldExtractor {

    private static final String DEFAULT_NOT_FOUND_VALUE = "N/A";
    private final SeleniumHelper seleniumHelper;

    /**
     * Extract a value from an element using the specified field selector
     */
    public String extractValue(WebElement element, FieldSelector fieldSelector) {
        if (element == null || fieldSelector == null) {
            return DEFAULT_NOT_FOUND_VALUE;
        }

        try {
            // Find the target element using the selector
            Optional<WebElement> targetElement = seleniumHelper.findElement(element, fieldSelector.elementLocator());
            targetElement.ifPresent(e -> System.out.println("Found target element " + e + " for selector " + fieldSelector.elementLocator()));

            if (targetElement.isEmpty()) {
                log.info("Target element not found for selector: {}", fieldSelector.elementLocator());
                return DEFAULT_NOT_FOUND_VALUE;
            }

            // Click the element if required before extraction
            if (fieldSelector.clickBeforeExtract()) {
                try {
                    targetElement.get().click();
                    // Wait a moment for any dynamic content to load
                    Thread.sleep(500);
                } catch (Exception e) {
                    log.debug("Failed to click element before extraction", e);
                }
            }

            // Extract the value using the specified extraction type
            String value = targetElement.map(e -> seleniumHelper.extractValueByType(e, fieldSelector.attributeSelector())).orElse(null);

            if (fieldSelector.regexPattern() != null && !fieldSelector.regexPattern().isBlank()) {
                value = applyRegexPattern(value, fieldSelector.regexPattern(), DEFAULT_NOT_FOUND_VALUE);
            }

            log.info("Extracted value: {}", value);

            return value != null && !value.isEmpty() ? value : DEFAULT_NOT_FOUND_VALUE;

        } catch (Exception e) {
            log.debug("Error extracting field value", e);
            return DEFAULT_NOT_FOUND_VALUE;
        }
    }
}