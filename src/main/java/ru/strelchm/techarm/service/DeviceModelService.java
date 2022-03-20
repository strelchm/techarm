package ru.strelchm.techarm.service;

import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.UserDto;

import java.util.List;
import java.util.UUID;

@Service
public interface DeviceModelService {
    List<DeviceModel> getAll();

    DeviceModel getById(UUID id);

    UUID add(DeviceModel deviceModel, UserDto userDto);

    DeviceModel edit(DeviceModel dto, UserDto userDto);

    void delete(UUID id, UserDto userDto);
}
