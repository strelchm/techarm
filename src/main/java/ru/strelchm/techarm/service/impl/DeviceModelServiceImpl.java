package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.domain.repo.DeviceModelRepository;
import ru.strelchm.techarm.dto.UserAppRole;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.service.DeviceModelService;

import java.util.UUID;

@Service
public class DeviceModelServiceImpl implements DeviceModelService {
    private final DeviceModelRepository devModelRepository;

    @Autowired
    public DeviceModelServiceImpl(DeviceModelRepository repository) {
        this.devModelRepository = repository;
    }

    public Page<DeviceModel> getAll(Specification<DeviceModel> deviceModelSpecification, Pageable pageable) {
        return devModelRepository.findAll(deviceModelSpecification, pageable);
    }

    @Override
    public DeviceModel getById(UUID id) {
        return getDevModelById(id);
    }

    @Override
    public UUID add(DeviceModel deviceModel, UserDto userDto) {
        if (deviceModel.getName() == null) {
            throw new BadRequestException("Device model name not existed");
        }
        if (deviceModel.getManufacture() == null) {
            throw new BadRequestException("Manufacture not existed");
        }
        if (deviceModel.getDeviceTypeKey() == null) {
            throw new BadRequestException("Device type key not existed");
        }
        if (devModelRepository.findByName(deviceModel.getName()).isPresent()) {
            throw new BadRequestException(String.format("Device model with name %s existed", deviceModel.getName()));
        }
        if (userDto.getUserAppRole() == null || userDto.getUserAppRole() == UserAppRole.CLIENT) {
            throw new BadRequestException("Only admin can register device model");
        }
        return devModelRepository.save(deviceModel).getId();
    }

    @Override
    public DeviceModel edit(DeviceModel dto, UserDto userDto) {
        if (userDto.getUserAppRole() != UserAppRole.ADMIN) { // редактировать можно только админу
            throw new AccessDeniedException();
        }

        DeviceModel deviceModel = getDevModelById(dto.getId());

        if (dto.getName() != null && !deviceModel.getName().equals(dto.getName())) {
            deviceModel.setName(dto.getName());
        }

        if (dto.getDeviceTypeKey() != null && (deviceModel.getDeviceTypeKey() == null ||
                !deviceModel.getDeviceTypeKey().equals(dto.getDeviceTypeKey()))) {
            deviceModel.setDeviceTypeKey(dto.getDeviceTypeKey());
        }

        if (dto.getManufacture() != null && !deviceModel.getManufacture().equals(dto.getManufacture())) {
            deviceModel.setManufacture(dto.getManufacture());
        }

        if (dto.getDocsSrc() != null && !deviceModel.getDocsSrc().equals(dto.getDocsSrc())) {
            deviceModel.setDocsSrc(dto.getDocsSrc());
        }

        if (dto.getImageSrc() != null && !deviceModel.getImageSrc().equals(dto.getImageSrc())) {
            deviceModel.setImageSrc(dto.getImageSrc());
        }

        return devModelRepository.save(deviceModel);
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        DeviceModel deviceModel = getDevModelById(id);
        devModelRepository.delete(deviceModel);
    }

    private DeviceModel getDevModelById(UUID id) {
        return devModelRepository.findById(id).orElseThrow(() -> new NotFoundException("Device model with id " + id + " not found"));
    }

    public DeviceModel getDevModelByName(String name) {
        return devModelRepository.findByName(name).orElseThrow(() -> new NotFoundException("Device model with name " + name + " not found"));
    }
}
