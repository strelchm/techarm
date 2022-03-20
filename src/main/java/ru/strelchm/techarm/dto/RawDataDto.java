package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.RawDataStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawDataDto {
    private UUID id;

    private UUID deviceId;

    private UUID userId;

    private RawDataStatus status;

    private String errorDescription;

    private Date created;

    private Date updated;
}
