package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.AttributeType;

public record AttributeSelector(AttributeType attributeType, String attributeName) {
    public AttributeSelector {
        if (attributeType == null) {
            attributeType = AttributeType.TEXT; // default value
        }
        if (attributeName == null) {
            attributeName = ""; // default value
        }
    }
}
