package ru.strelchm.techarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasStatDto {
    private Date start;
    private Date end;
    private List<DataStatDto> data;
}
