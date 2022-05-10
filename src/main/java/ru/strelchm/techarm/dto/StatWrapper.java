package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatWrapper {
    private Long count;
    private Double max;
    private Double min;
    private Double average;
    private UUID deviceId;
    private String functionKey;
}
