package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class UserContext {
    private Optional<UserDto> user;
}
