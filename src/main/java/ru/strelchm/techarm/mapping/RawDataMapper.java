package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.dto.DeviceDto;
import ru.strelchm.techarm.dto.RawDataDto;

@Mapper(componentModel = "spring")
public interface RawDataMapper {
    RawDataDto toRawDataDto(RawData rawData);

    RawData fromRawDataDto(RawDataDto dto);
}
