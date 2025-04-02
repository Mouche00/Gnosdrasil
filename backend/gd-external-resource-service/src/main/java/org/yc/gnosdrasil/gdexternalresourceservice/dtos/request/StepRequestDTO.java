package org.yc.gnosdrasil.gdexternalresourceservice.dtos.request;

import java.util.List;

public record StepRequestDTO(String id, String label, List<String> userId) {
}