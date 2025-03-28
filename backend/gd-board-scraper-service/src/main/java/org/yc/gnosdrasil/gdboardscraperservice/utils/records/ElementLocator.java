package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.LocatorType;

public record ElementLocator(LocatorType locatorType, String locatorString) {}