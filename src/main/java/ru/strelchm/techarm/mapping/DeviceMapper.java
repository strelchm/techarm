package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.DeviceDto;
import ru.strelchm.techarm.dto.DeviceModelDto;
import ru.strelchm.techarm.dto.DeviceResponseDto;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDto toDeviceDto(Device device);

    DeviceResponseDto toDeviceResponseDto(Device device);

    Device fromDeviceDto(DeviceDto dto);
}
