package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.common.AttributeType;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.AttributeSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.FieldSelector;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts field values from WebElements using different strategies
 */
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

            if (targetElement.isEmpty()) {
                log.debug("Target element not found for selector: {}", fieldSelector.elementLocator());
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
            String value = seleniumHelper.extractValueByType(targetElement.get(), fieldSelector.attributeSelector());

            return value != null && !value.isEmpty() ? value : DEFAULT_NOT_FOUND_VALUE;

        } catch (Exception e) {
            log.debug("Error extracting field value", e);
            return DEFAULT_NOT_FOUND_VALUE;
        }
    }
}