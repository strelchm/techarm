package ru.strelchm.techarm.service;

import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.dto.UserDto;

public interface JwtTokenService {
    String generateToken(User user);

    UserDto parseToken(String token);
}