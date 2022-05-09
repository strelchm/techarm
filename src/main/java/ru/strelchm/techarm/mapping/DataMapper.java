package ru.strelchm.techarm.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.strelchm.techarm.domain.Data;
import ru.strelchm.techarm.domain.RawData;
import ru.strelchm.techarm.dto.DataCreateDto;
import ru.strelchm.techarm.dto.DataDto;
import ru.strelchm.techarm.dto.RawDataDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataMapper {
//    @Mapping(source = "rawData.device.id", target = "deviceId")
//    @Mapping(source = "rawData.user.id", target = "userId")
    DataDto toDataDto(Data data);

    DataCreateDto toDataCreateDto(Data data);

    List<Data> fromDataListDto(List<DataDto> data);

    List<Data> fromDataCreateListDto(List<DataCreateDto> data);

//    RawData fromRawDataDto(RawDataDto dto);
}
