package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

import java.util.List;

public record SearchParamsResponseDTO(List<String> keywords, String experienceLevel, String location, String date) {
}
