package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.dto.RawDataDto;

@Mapper(componentModel = "spring")
public interface RawDataMapper {
    @Mapping(source = "rawData.device.id", target = "deviceId")
    @Mapping(source = "rawData.user.id", target = "userId")
    RawDataDto toRawDataDto(RawData rawData);

    RawData fromRawDataDto(RawDataDto dto);
}
