package org.yc.gnosdrasil.gdboardscraperservice.utils.enums.common;

/**
 * Types of data extraction methods
 */
public enum AttributeType {
    TEXT,           // For text content
    ATTRIBUTE,      // For an attribute value
    HREF,           // For href attribute (shorthand)
    INNER_HTML,     // For innerHTML
    COMPUTED_STYLE  // For computed CSS style
}