package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.DeviceDto;
import ru.strelchm.techarm.dto.DeviceModelDto;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDto toDeviceDto(Device device);

    Device fromDeviceDto(DeviceDto dto);
}
