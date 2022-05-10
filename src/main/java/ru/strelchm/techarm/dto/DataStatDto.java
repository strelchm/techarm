package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.strelchm.techarm.domain.RawDataStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataStatDto {
    private Long count;
}
