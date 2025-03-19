package org.yc.gnosdrasil.gdexternalresourceservice.dtos.response;

public record SerpApiResponseDTO(int position,
                                 String title,
                                 String link,
                                 String snippet) {
}
