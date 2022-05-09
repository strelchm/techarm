package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataCreateListDto {
    @NotNull
    private List<DataCreateDto> data;

    @NotNull
    private UUID deviceId;

    @NotNull
    private UUID rawDataId;
}
