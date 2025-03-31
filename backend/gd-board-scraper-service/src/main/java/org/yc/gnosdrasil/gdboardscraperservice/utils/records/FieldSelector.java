package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import lombok.Builder;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.AttributeType;

/**
 * Configuration for extracting a specific field
 */
@Builder
public record FieldSelector(
        ElementLocator elementLocator,
        AttributeSelector attributeSelector,
        String regexPattern,
        boolean clickBeforeExtract
) {
    public FieldSelector {
        if (attributeSelector == null) {
            attributeSelector = new AttributeSelector(AttributeType.TEXT, "");
        }
    }
}