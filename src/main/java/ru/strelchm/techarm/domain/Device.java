package ru.strelchm.techarm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

/**
 * Устройство
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device extends ParentEntity {
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "\\S{2,50}")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    @NotNull
    private DeviceModel model;

    private Date lastParsedDate;
}
