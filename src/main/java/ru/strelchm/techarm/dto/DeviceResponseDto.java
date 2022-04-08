package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDto {
    private UUID id;

    private String name;

    private DeviceModelDto model;

    private Date lastParsedDate;

    private Date created;

    private Date updated;
}
