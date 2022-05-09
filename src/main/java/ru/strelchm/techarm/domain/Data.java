package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Данные устройства
 */
@Entity
@lombok.Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Data extends ParentEntity {
    @ManyToOne
    @JoinColumn(name = "raw_data_id")
    @NotNull
    private RawData rawData;
    @ManyToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;

    private Date processedTime;

    @Enumerated(EnumType.STRING)
    private DataType type;

    private String functionKey;

    private String value;

    @Enumerated(EnumType.STRING)
    private RawDataStatus status;
}
