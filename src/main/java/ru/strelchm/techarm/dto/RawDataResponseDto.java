package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.RawDataStatus;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawDataResponseDto {
    private UUID id;

    private DeviceResponseDto device;

    private UUID userId;

    private String data;

    private RawDataStatus status;

    private String errorDescription;

    private Date processedTime;

    private Date created;

    private Date updated;
}
