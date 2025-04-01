package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

import java.util.List;

public record SearchParamsDTO(List<String> keywords, String experienceLevel, String location, String date) {
}
