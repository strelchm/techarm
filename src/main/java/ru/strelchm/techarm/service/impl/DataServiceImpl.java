package ru.strelchm.techarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.strelchm.techarm.domain.*;
import ru.strelchm.techarm.domain.repo.DataRepository;
import ru.strelchm.techarm.dto.*;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.mapping.DataMapper;
import ru.strelchm.techarm.service.DataService;
import ru.strelchm.techarm.service.DeviceService;
import ru.strelchm.techarm.service.RawDataService;
import ru.strelchm.techarm.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;
    private final DataMapper dataMapper;
    private final DeviceService deviceService;
    private final RawDataService rawDataService;
    private final UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository, DataMapper dataMapper,
                           DeviceService deviceService, UserService userService, RawDataService rawDataService) {
        this.dataRepository = dataRepository;
        this.dataMapper = dataMapper;
        this.deviceService = deviceService;
        this.userService = userService;
        this.rawDataService = rawDataService;
    }

    @Override
    public List<Data> getAll(Specification<Data> dataSpecification) {
        return dataRepository.findAll(dataSpecification);
    }

    @Override
    public Long getStatistics(Date start, Date end, UUID deviceId, String functionKey) {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM data d ");
        sb.append("WHERE ");
        if (start != null) {
            sb.append("d.processed_time > (:start) ");
        }
        if (end != null) {
            if (start != null) {
                sb.append("AND ");
            }
            sb.append("d.processed_time < (:end) ");
        }
        if (deviceId != null) {
            if (start != null || end != null) {
                sb.append("AND ");
            }
            sb.append("d.device_id = :deviceId ");
        } else {
            sb.append("d.device_id IN (SELECT d.id FROM device d) ");
        }
        if(functionKey != null) {
            if (start != null || end != null || deviceId != null) {
                sb.append("AND ");
            }
            sb.append("d.function_key = (:functionKey) ");
        }
        sb.append("GROUP BY d.device_id, d.functionKey ");

        Query q = entityManager.createNativeQuery(sb.toString(), Long.class);
        if (start != null) {
            q.setParameter("start", start);
        }
        if (end != null) {
            q.setParameter("end", end);
        }
        if (deviceId != null) {
            q.setParameter("deviceId", deviceId);
        }

        if (functionKey != null) {
            q.setParameter("functionKey", functionKey);
        }

        return (Long) q.getSingleResult();
    }

    @Override
    public List<Data> add(DataCreateListDto dataDto) {
        Device device = deviceService.getById(dataDto.getDeviceId());
        if (device == null) {
            throw new BadRequestException("Device not found");
        }
        RawData rawData = rawDataService.getById(dataDto.getRawDataId());
        if (rawData == null) {
            throw new BadRequestException("Raw data not found");
        }

        List<Data> data = dataMapper.fromDataCreateListDto(dataDto.getData());
        data.forEach(d -> {
            d.setDevice(device);
            d.setRawData(rawData);
        });

        return dataRepository.saveAll(data);
    }
}
