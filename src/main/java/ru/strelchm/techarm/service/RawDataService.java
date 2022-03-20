package ru.strelchm.techarm.service;

import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface RawDataService {
    List<RawData> getAll();

    RawData getById(UUID id);

    UUID add(RawData device, UserDto userDto);

    RawData setStatus(RawData rawData, RawDataStatus status);

    RawData setStatus(UUID rawDataId, RawDataStatus status);

    RawData setError(RawData rawData, String errorDescription);

    RawData setError(UUID rawDataId, String errorDescription);

    void delete(UUID id, UserDto userDto);
}
