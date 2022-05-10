package ru.strelchm.techarm.service;

import org.springframework.data.jpa.domain.Specification;
import ru.strelchm.techarm.domain.Data;
import ru.strelchm.techarm.dto.DataCreateListDto;
import ru.strelchm.techarm.dto.StatWrapper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface DataService {
    List<Data> getAll(Specification<Data> dataSpecification);

    List<Data> add(DataCreateListDto dataDto);

    List<StatWrapper> getStatistics(Date startDate, Date endDate, UUID deviceId, String functionKey);
}
