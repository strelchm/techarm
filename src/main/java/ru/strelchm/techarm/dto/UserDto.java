package ru.strelchm.techarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.UserStatus;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String login;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private UserStatus status;
    private UserAppRole userAppRole;
    private Date created;
    private Date updated;
}
