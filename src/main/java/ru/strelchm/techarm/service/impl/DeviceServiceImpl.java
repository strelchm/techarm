package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.domain.repo.DeviceModelRepository;
import ru.strelchm.techarm.domain.repo.DeviceRepository;
import ru.strelchm.techarm.dto.UserAppRole;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.service.DeviceService;

import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceModelRepository deviceModelRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceModelRepository deviceModelRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceModelRepository = deviceModelRepository;
    }

    @Override
    public Page<Device> getAll(Specification<Device> deviceSpecification, Pageable pageable) {
        return deviceRepository.findAll(deviceSpecification, pageable);
    }

    @Override
    public Device getById(UUID id) {
        return getDeviceById(id);
    }

    @Override
    public UUID add(Device device, UUID deviceModelId,  UserDto userDto) {
        if (device.getName() == null) {
            throw new BadRequestException("Device name not existed");
        }
        if (deviceModelId == null) {
            throw new BadRequestException("Model id not existed");
        }
        if (deviceRepository.findByName(device.getName()).isPresent()) {
            throw new BadRequestException(String.format("Device with name %s existed", device.getName()));
        }
        DeviceModel model = deviceModelRepository.findById(deviceModelId).orElse(null);
        if (model == null) {
            throw new BadRequestException("Model id not found");
        }
        device.setModel(model);
        return deviceRepository.save(device).getId();
    }

    @Override
    public Device edit(Device dto, UUID deviceModelId, UserDto userDto) {
        if (userDto.getUserAppRole() != UserAppRole.ADMIN) { // редактировать можно только админу
            throw new AccessDeniedException();
        }

        Device device = getDeviceById(dto.getId());

        if (dto.getName() != null && !device.getName().equals(dto.getName())) {
            device.setName(dto.getName());
        }

        if (deviceModelId != null && !device.getModel().getId().equals(deviceModelId)) {
            DeviceModel model = deviceModelRepository.findById(deviceModelId).orElse(null);
            if (model == null) {
                throw new BadRequestException("Model id not found");
            }
            device.setModel(model);
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
