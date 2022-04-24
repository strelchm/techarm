package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.domain.RawDataStatus;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.repo.RawDataRepository;
import ru.strelchm.techarm.dto.RawDataDto;
import ru.strelchm.techarm.dto.RawDataStatDto;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.mapping.RawDataMapper;
import ru.strelchm.techarm.service.DeviceService;
import ru.strelchm.techarm.service.RawDataService;
import ru.strelchm.techarm.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RawDataServiceImpl implements RawDataService {
    private final RawDataRepository rawDataRepository;
    private final RawDataMapper rawDataMapper;
    private final DeviceService deviceService;
    private final UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public RawDataServiceImpl(RawDataRepository rawDataRepository, RawDataMapper rawDataMapper,
                              DeviceService deviceService, UserService userService) {
        this.rawDataRepository = rawDataRepository;
        this.rawDataMapper = rawDataMapper;
        this.deviceService = deviceService;
        this.userService = userService;
    }

    @Override
    public List<RawData> getAll() {
        return rawDataRepository.findAll();
    }

    @Override
    public List<RawDataStatDto> getStatistics(
            @Param("start") @Temporal Date start,
            @Param("end") @Temporal Date end,
            @Param("deviceIds") List<UUID> deviceIds
    ) {
        StringBuilder sb = new StringBuilder("SELECT new ru.strelchm.techarm.dto.RawDataStatDto(rd.status, COUNT(rd), rd.device.id) FROM RawData rd ");
        sb.append("WHERE ");
        if (start != null) {
            sb.append("rd.processedTime > (:start) ");
        }
        if (end != null) {
            if (start != null) {
                sb.append("AND ");
            }
            sb.append("rd.processedTime < (:end) ");
        }
        if (deviceIds != null) {
            if (start != null || end != null) {
                sb.append("AND ");
            }
            sb.append("rd.device.id IN (:deviceIds) ");
        } else {
            sb.append("rd.device.id IN (SELECT d.id FROM Device d) ");
        }
        sb.append("GROUP BY rd.status, rd.device.id ");

        Query q = entityManager.createQuery(sb.toString(), RawDataStatDto.class);
        if (start != null) {
            q.setParameter("start", start);
        }
        if (end != null) {
            q.setParameter("end", end);
        }
        if (deviceIds != null) {
            q.setParameter("deviceIds", deviceIds);
        }


        return q.getResultList();
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
