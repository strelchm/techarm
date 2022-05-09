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
import java.util.List;

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

//    @Override
//    public List<RawDataStatDto> getStatistics(
//            @Param("start") @Temporal Date start,
//            @Param("end") @Temporal Date end,
//            @Param("rawDataIds") List<UUID> rawDataIds
//    ) {
//        StringBuilder sb = new StringBuilder("SELECT new ru.strelchm.techarm.dto.RawDataStatDto(rd.status, COUNT(rd), rd.device.id) FROM RawData rd ");
//        sb.append("WHERE ");
//        if (start != null) {
//            sb.append("rd.processedTime > (:start) ");
//        }
//        if (end != null) {
//            if (start != null) {
//                sb.append("AND ");
//            }
//            sb.append("rd.processedTime < (:end) ");
//        }
//        if (deviceIds != null) {
//            if (start != null || end != null) {
//                sb.append("AND ");
//            }
//            sb.append("rd.device.id IN (:deviceIds) ");
//        } else {
//            sb.append("rd.device.id IN (SELECT d.id FROM Device d) ");
//        }
//        sb.append("GROUP BY rd.status, rd.device.id ");
//
//        Query q = entityManager.createQuery(sb.toString(), RawDataStatDto.class);
//        if (start != null) {
//            q.setParameter("start", start);
//        }
//        if (end != null) {
//            q.setParameter("end", end);
//        }
//        if (deviceIds != null) {
//            q.setParameter("deviceIds", deviceIds);
//        }
//
//
//        return q.getResultList();
//    }

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

//    @Override
//    public void delete(UUID id, UserDto userDto) {
//        RawData device = getRawDataById(id);
//        dataRepository.delete(device);
//    }
}
