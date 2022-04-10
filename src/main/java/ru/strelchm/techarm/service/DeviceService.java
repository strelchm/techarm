package ru.strelchm.techarm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.dto.UserDto;

import java.util.UUID;

public interface DeviceService {
    Page<Device> getAll(Specification<Device> deviceSpecification, Pageable pageable);

    Device getById(UUID id);

    UUID add(Device device, UUID deviceModelId, UserDto userDto);

    Device edit(Device dto, UUID deviceModelId, UserDto userDto);

    void delete(UUID id, UserDto userDto);
}
