package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.JobField;

public record JobSelector(JobField field, FieldSelector selector) {
}
