package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.DataType;
import ru.strelchm.techarm.domain.RawDataStatus;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCreateDto {
    private UUID id;

    private String value;

    private Date processedTime;

    private DataType type;

    private String functionKey;

    private RawDataStatus status;
}
