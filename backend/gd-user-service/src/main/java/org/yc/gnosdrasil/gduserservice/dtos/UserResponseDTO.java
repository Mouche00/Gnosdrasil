package org.yc.gnosdrasil.gduserservice.dtos;

public record UserResponseDTO(Long id,
                              String username,
                              String email,
                              String password) {
}
