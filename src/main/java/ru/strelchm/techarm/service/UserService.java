package ru.strelchm.techarm.service;

import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserDto> getAll();

    UserDto getSelf(UserDto dto);

    UserDto getById(UUID id);

    UUID add(UserDto dto);

    UserDto edit(UserDto dto, UserDto userDto);

    void delete(UUID id, UserDto userDto);

    User getUserById(UUID id);

    Optional<UserDto> getUserByLogin(String login);

    void blockUser(UUID userId, UserDto userDto);

    void unblockUser(UUID userId, UserDto userDto);
}
