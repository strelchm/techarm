package ru.strelchm.techarm.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.dto.UserDto;

@Service
public class MappingUtil {
    @Autowired
   private UserMapper userMapper;

    public UserDto mapToUserDto(User entity) {
        return userMapper.toDto(entity);
    }

    public User mapToUserEntity(UserDto dto) {
        return userMapper.fromDto(dto);
    }
}
