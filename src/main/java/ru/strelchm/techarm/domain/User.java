package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.dto.UserAppRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Модель пользователя системы
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends ParentEntity {
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "\\S{3,50}")
    private String login;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserAppRole userAppRole;
}
