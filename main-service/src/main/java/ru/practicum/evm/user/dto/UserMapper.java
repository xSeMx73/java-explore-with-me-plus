package ru.practicum.evm.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.evm.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
