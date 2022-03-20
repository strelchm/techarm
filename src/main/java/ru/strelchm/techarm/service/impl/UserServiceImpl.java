package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.UserStatus;
import ru.strelchm.techarm.domain.repo.UserRepository;
import ru.strelchm.techarm.dto.UserAppRole;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.mapping.MappingUtil;
import ru.strelchm.techarm.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final MappingUtil mappingUtil;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder, MappingUtil mappingUtil) {
        this.userRepository = repository;
        this.encoder = encoder;
        this.mappingUtil = mappingUtil;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(mappingUtil::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getSelf(UserDto dto) {
        return getById(dto.getId());
    }

    @Override
    public UserDto getById(UUID id) {
        return mappingUtil.mapToUserDto(getUserById(id));
    }

    @Override
    public UUID add(UserDto dto) {
        if (userRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new BadRequestException(String.format("User with login %s existed", dto.getLogin()));
        }
        if (dto.getUserAppRole() == null) {
            dto.setUserAppRole(UserAppRole.CLIENT);
        } else if (dto.getUserAppRole() == UserAppRole.ADMIN) {
            throw new BadRequestException("Can't register admin during standard registration attempt");
        }
        dto.setPassword(encoder.encode(dto.getPassword()));
        dto.setStatus(UserStatus.ACTIVE);
        User user = userRepository.save(mappingUtil.mapToUserEntity(dto));
        return userRepository.save(user).getId();
    }

    @Override
    public UserDto edit(UserDto dto, UserDto userDto) {
        if (!dto.getId().equals(userDto.getId()) && userDto.getUserAppRole() != UserAppRole.ADMIN) { // редактировать можно только самому себя или админу
            throw new AccessDeniedException();
        }

        User user = getUserById(dto.getId());

        if (dto.getLogin() != null && !user.getLogin().equals(dto.getLogin())) {
            user.setLogin(dto.getLogin());
        }

        if (dto.getStatus() != null && !user.getStatus().equals(dto.getStatus())) {
            user.setStatus(dto.getStatus());

//            if (dto.getStatus() == UserStatus.GLOBAL_BLOCKED) {
//                blockedUserRepository.save(new BlockedUser(dto.getId()));
//            }
        }

        return mappingUtil.mapToUserDto(userRepository.save(user));
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        User user = getUserById(id);

        if (!id.equals(userDto.getId()) && userDto.getUserAppRole() != UserAppRole.ADMIN) { // удалить можно только самому себя или админу
            throw new AccessDeniedException();
        }

        userRepository.delete(user);
    }

    @Override
    public void blockUser(UUID userId, UserDto userDto) {
        if(userDto.getUserAppRole() != UserAppRole.ADMIN) {
            throw new AccessDeniedException("Only admin can block users");
        }
        changeUserStatusByAdmin(userId, userDto, UserStatus.GLOBAL_BLOCKED);
//        blockedUserRepository.save(new BlockedUser(userId));
    }

    @Override
    public void unblockUser(UUID userId, UserDto userDto) {
        if(userDto.getUserAppRole() != UserAppRole.ADMIN) {
            throw new AccessDeniedException("Only admin can unblock users");
        }
        changeUserStatusByAdmin(userId, userDto, UserStatus.ACTIVE);
//        blockedUserRepository.deleteById(userId);
    }

    private void changeUserStatusByAdmin(UUID userId, UserDto userDto, UserStatus status) {
        User user = getUserById(userId);

        if (user.getStatus() == status) {
            throw new BadRequestException("Status is already " + status);
        }

        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new NotFoundException("User with login " + login + " not found"));
        return Optional.ofNullable(mappingUtil.mapToUserDto(user));
    }
}
