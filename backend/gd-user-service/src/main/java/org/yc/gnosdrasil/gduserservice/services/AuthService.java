package org.yc.gnosdrasil.gduserservice.services;

import org.yc.gnosdrasil.gduserservice.dtos.JwtResponseDTO;
import org.yc.gnosdrasil.gduserservice.dtos.LoginRequestDTO;
import org.yc.gnosdrasil.gduserservice.dtos.RegisterRequestDTO;
import org.yc.gnosdrasil.gduserservice.dtos.UserResponseDTO;

public interface AuthService {
    JwtResponseDTO login(LoginRequestDTO body);
    JwtResponseDTO register(RegisterRequestDTO dto);
}
