package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.DataType;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataDto {
    private UUID id;

    private String value;

    private Date processedTime;

    private DataType type;

    private String functionKey;

    private RawDataStatus status;
}
