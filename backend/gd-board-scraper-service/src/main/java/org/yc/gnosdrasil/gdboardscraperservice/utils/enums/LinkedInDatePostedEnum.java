package org.yc.gnosdrasil.gdboardscraperservice.utils.enums;

import lombok.Getter;

@Getter
public enum LinkedInDatePostedEnum {
    PAST_24_HOURS("r86400", "Past 24 hours"),
    PAST_WEEK("r604800", "Past week"),
    PAST_MONTH("r2592000", "Past month");


    LinkedInDatePostedEnum(String id, String label) {

    }

//    @Override
//    public String toString() {
//        return label;
//    }
}
