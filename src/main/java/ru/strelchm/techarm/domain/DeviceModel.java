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
 * Модель устройства
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_models")
public class DeviceModel extends ParentEntity {
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "\\S{2,50}")
    private String name;

    @NotNull
    @Size(min = 8, max = 100)
    private String manufacture;

    private String imageSrc;

    private String docsSrc;
}
