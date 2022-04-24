package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.RawDataStatus;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawDataStatusDto {
    @NotNull
    private RawDataStatus status;
    @NotNull
    private Long processedTime;
}
