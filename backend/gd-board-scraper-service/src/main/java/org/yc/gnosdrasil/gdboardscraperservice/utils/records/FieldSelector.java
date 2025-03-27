package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import lombok.Builder;

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
}