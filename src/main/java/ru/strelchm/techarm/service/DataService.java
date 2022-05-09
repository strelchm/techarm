package ru.strelchm.techarm.service;

import org.springframework.data.jpa.domain.Specification;
import ru.strelchm.techarm.domain.Data;
import ru.strelchm.techarm.dto.DataCreateListDto;

import java.util.List;

public interface DataService {
    List<Data> getAll(Specification<Data> dataSpecification);

//    List<RawDataStatDto> getStatistics(
//            @Param("start") @Temporal Date start,
//            @Param("end") @Temporal Date end,
//            @Param("rawDataIds") List<UUID> rawDataIds
//    );

    List<Data> add(DataCreateListDto dataDto);
}
