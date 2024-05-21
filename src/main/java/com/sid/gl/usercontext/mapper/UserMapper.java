package com.sid.gl.usercontext.mapper;

import com.sid.gl.usercontext.domain.User;
import com.sid.gl.usercontext.dto.ReadUserDTO;
import com.sid.gl.usercontext.dto.UserRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ReadUserDTO readUserDTOToUser(User entity);

    User userRequestDTOToUser(UserRequestDTO dto);
}
