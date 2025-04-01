package org.yc.gnosdrasil.gdboardscraperservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchParamsDTO(@NotNull @NotEmpty List<String> keywords, String experienceLevel, @NotNull @NotEmpty String location, String date) {
}
