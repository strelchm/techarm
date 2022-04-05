package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
    @NotNull
    private UUID deviceId;

    @NotNull
    private UUID userId;

    private RawDataStatus status;

    private String errorDescription;
}
