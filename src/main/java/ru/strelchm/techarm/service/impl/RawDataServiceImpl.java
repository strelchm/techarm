package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.repo.RawDataRepository;
import ru.strelchm.techarm.dto.RawDataDto;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.mapping.RawDataMapper;
import ru.strelchm.techarm.service.DeviceService;
import ru.strelchm.techarm.service.RawDataService;
import ru.strelchm.techarm.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class RawDataServiceImpl implements RawDataService {
    private final RawDataRepository rawDataRepository;
    private final RawDataMapper rawDataMapper;
    private final DeviceService deviceService;
    private final UserService userService;

    @Autowired
    public RawDataServiceImpl(RawDataRepository rawDataRepository, RawDataMapper rawDataMapper,
                              DeviceService deviceService, UserService userService) {
        this.rawDataRepository = rawDataRepository;
        this.rawDataMapper = rawDataMapper;
        this.deviceService = deviceService;
        this.userService = userService;
    }

    @Autowired


    @Override
    public List<RawData> getAll() {
        return rawDataRepository.findAll();
    }

    @Override
    public RawData getById(UUID id) {
        return getRawDataById(id);
    }

    @Override
    public UUID add(RawDataDto rawDataDto, UserDto userDto) {
        RawData rawData = rawDataMapper.fromRawDataDto(rawDataDto);

        Device device = deviceService.getById(rawDataDto.getDeviceId());
        if (device == null) {
            throw new BadRequestException("Device not found");
        }
        rawData.setDevice(device);

        User user = userService.getUserById(rawDataDto.getUserId());
        if (user == null) {
            throw new BadRequestException("User not found");
        }
        rawData.setUser(user);

        return rawDataRepository.save(rawData).getId();
    }

    @Override
    public RawData setStatus(UUID rawDataId, RawDataStatus status) {
        return setStatus(rawDataId, status);
    }

    @Override
    public RawData setStatus(RawData rawData, RawDataStatus status) {
        rawData.setStatus(status);
        return rawDataRepository.save(rawData);
    }

    @Override
    public RawData setError(RawData rawData, String errorDescription) {
        rawData.setStatus(RawDataStatus.ERROR);
        rawData.setErrorDescription(errorDescription);
        return rawDataRepository.save(rawData);
    }

    @Override
    public RawData setError(UUID rawDataId, String errorDescription) {
        RawData rawData = getRawDataById(rawDataId);
        return setError(rawData, errorDescription);
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        RawData device = getRawDataById(id);
        rawDataRepository.delete(device);
    }

    private RawData getRawDataById(UUID id) {
        return rawDataRepository.findById(id).orElseThrow(() -> new NotFoundException("Raw data with id " + id + " not found"));
    }
}
