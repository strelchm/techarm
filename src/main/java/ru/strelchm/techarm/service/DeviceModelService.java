package ru.strelchm.techarm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.UserDto;

import java.util.UUID;

@Service
public interface DeviceModelService {
    Page<DeviceModel> getAll(Specification<DeviceModel> deviceSpecification, Pageable pageable);

    DeviceModel getById(UUID id);

    UUID add(DeviceModel deviceModel, UserDto userDto);

    DeviceModel edit(DeviceModel dto, UserDto userDto);

    void delete(UUID id, UserDto userDto);
}
