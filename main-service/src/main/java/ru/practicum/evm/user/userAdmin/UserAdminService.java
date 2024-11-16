package ru.practicum.evm.user.userAdmin;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.evm.exceptions.InternalServerException;
import ru.practicum.evm.exceptions.NotFoundException;
import ru.practicum.evm.user.dto.UserDto;
import ru.practicum.evm.user.dto.UserMapper;
import ru.practicum.evm.user.model.User;
import ru.practicum.evm.user.repository.UserRepository;


import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAdminService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        try {
            userDto = userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
            if (userDto.id() == null) {
                throw new InternalServerException("Не удалось сохранить данные");
            }
        return userDto;
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Пользователь с таким email уже существует",new SQLException(),"Email должен быть уникальным");
        }
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null) {
            users = userRepository.findAll(pageable);
        } else {
          users = userRepository.findUsers(ids,pageable);
      }
    return users.stream().map(userMapper::toUserDto).toList();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.delete(user);
    }
}