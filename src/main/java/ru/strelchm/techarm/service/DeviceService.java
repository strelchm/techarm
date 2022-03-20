package ru.strelchm.techarm.service;

import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    List<Device> getAll();

    Device getById(UUID id);

    UUID add(Device device, UserDto userDto);

    Device edit(Device dto, UserDto userDto);

    void delete(UUID id, UserDto userDto);
}
