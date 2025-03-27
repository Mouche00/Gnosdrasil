package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.common.LocatorType;

public record ElementLocator(LocatorType locatorType, String locatorString) {}