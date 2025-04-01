package org.yc.gnosdrasil.gduserservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(@NotEmpty @NotNull String username,
                                 @NotEmpty @NotNull String email,
                                 @NotEmpty @NotNull String password) {
}
