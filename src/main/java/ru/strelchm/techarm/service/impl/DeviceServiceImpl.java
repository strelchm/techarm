package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.repo.DeviceRepository;
import ru.strelchm.techarm.dto.UserAppRole;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.service.DeviceService;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device getById(UUID id) {
        return getDeviceById(id);
    }

    @Override
    public UUID add(Device device, UserDto userDto) {
        if (device.getName() == null) {
            throw new BadRequestException("Device name not existed");
        }
        if (device.getModelId() == null) {
            throw new BadRequestException("Model id not existed");
        }
        if (deviceRepository.findByName(device.getName()).isPresent()) {
            throw new BadRequestException(String.format("Device with name %s existed", device.getName()));
        }
        return deviceRepository.save(device).getId();
    }

    @Override
    public Device edit(Device dto, UserDto userDto) {
        if (userDto.getUserAppRole() != UserAppRole.ADMIN) { // редактировать можно только админу
            throw new AccessDeniedException();
        }

        Device device = getDeviceById(dto.getId());

        if (dto.getName() != null && !device.getName().equals(dto.getName())) {
            device.setName(dto.getName());
        }

        if (dto.getModelId() != null && !device.getModelId().equals(dto.getModelId())) {
            device.setModelId(dto.getModelId());
        }

        return deviceRepository.save(device);
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        Device device = getDeviceById(id);
        deviceRepository.delete(device);
    }

    private Device getDeviceById(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Device with id " + id + " not found"));
    }

    public Device getDeviceByName(String name) {
        return deviceRepository.findByName(name).orElseThrow(() -> new NotFoundException("Device with name " + name + " not found"));
    }
}
