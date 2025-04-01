package org.yc.gnosdrasil.gduserservice.dtos;

public record JwtResponseDTO(String token, String type, Long id, String username, String email, String role) {
    public JwtResponseDTO {
        type = "Bearer";
    }
}

