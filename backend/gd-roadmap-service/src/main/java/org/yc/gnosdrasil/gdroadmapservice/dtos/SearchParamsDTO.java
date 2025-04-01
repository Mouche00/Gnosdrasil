package org.yc.gnosdrasil.gdroadmapservice.dtos;

import java.util.List;

public record SearchParamsDTO(List<String> keywords, String experienceLevel, String location, String date) {
}
