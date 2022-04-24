package ru.strelchm.techarm.service;

import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.dto.RawDataDto;
import ru.strelchm.techarm.dto.RawDataStatDto;
import ru.strelchm.techarm.dto.UserDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RawDataService {
    List<RawData> getAll();

    List<RawDataStatDto> getStatistics(Date start, Date end, List<UUID> deviceId);

    RawData getById(UUID id);

    UUID add(RawDataDto rawDataDto, UserDto userDto);

    RawData setStatus(UUID rawDataId, RawDataStatus status, Date processedTime);

    RawData setStatus(RawData rawData, RawDataStatus status, Date processedTime);

    RawData setError(RawData rawData, String errorDescription, Date processedTime);

    RawData setError(UUID rawDataId, String errorDescription, Date processedTime);

    void delete(UUID id, UserDto userDto);
}
