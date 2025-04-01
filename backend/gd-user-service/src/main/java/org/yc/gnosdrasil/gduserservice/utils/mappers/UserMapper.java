package org.yc.gnosdrasil.gduserservice.utils.mappers;

import org.mapstruct.Mapper;
import org.yc.gnosdrasil.gduserservice.dtos.RegisterRequestDTO;
import org.yc.gnosdrasil.gduserservice.dtos.UserResponseDTO;
import org.yc.gnosdrasil.gduserservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequestDTO dto);
    UserResponseDTO toDTO(User user);
}
