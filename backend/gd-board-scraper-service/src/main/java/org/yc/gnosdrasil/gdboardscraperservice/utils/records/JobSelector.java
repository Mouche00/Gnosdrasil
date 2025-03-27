package org.yc.gnosdrasil.gdboardscraperservice.utils.records;

import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.common.JobField;

public record JobSelector(JobField field, FieldSelector selector) {
}
