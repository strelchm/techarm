package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "password", expression = "java(null)")
    UserDto toDto(User user);

    User fromDto(UserDto dto);
}
