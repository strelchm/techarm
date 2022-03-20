package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    private UUID id;

    private String name;

    private UUID modelId;

    private Date lastParsedDate;

    private Date created;

    private Date updated;
}
