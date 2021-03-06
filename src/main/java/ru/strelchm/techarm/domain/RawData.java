package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Данные устройства
 */
@Entity
@lombok.Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raw_data")
public class RawData extends ParentEntity {
    @OneToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;

    @OneToMany(mappedBy = "rawData")
    private List<ru.strelchm.techarm.domain.Data> parsedData;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    private String data;

    private Date processedTime;

    private RawDataStatus status;

    private String errorDescription;
}
