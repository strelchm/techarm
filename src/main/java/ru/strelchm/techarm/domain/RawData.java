package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Данные устройства
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raw_data")
public class RawData extends ParentEntity {
    @OneToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    private RawDataStatus status;

    private String errorDescription;
}
