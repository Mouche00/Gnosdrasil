package org.yc.gnosdrasil.gdboardscraperservice.config.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;

@Getter
@Setter
@ToString
public class PopupConfig {
    private boolean enabled;
    private ElementLocator popupLocator;
    private ElementLocator closeButtonLocator;
    private int waitTimeMs;
} 