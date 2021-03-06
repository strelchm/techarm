package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseListDto {
    @NotNull
    private List<DeviceResponseDto> devices;
    @NotNull
    private Long count;
}
