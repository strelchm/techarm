package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.DeviceModelDto;

@Mapper(componentModel = "spring")
public interface DeviceModelMapper {
    DeviceModelDto toDeviceModelDto(DeviceModel user);

    DeviceModel fromDeviceModelDto(DeviceModelDto dto);
}
