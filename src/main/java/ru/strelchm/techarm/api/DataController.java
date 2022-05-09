package ru.strelchm.techarm.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.strelchm.techarm.domain.Data;
import ru.strelchm.techarm.domain.Device;
import ru.strelchm.techarm.dto.*;
import ru.strelchm.techarm.mapping.DataMapper;
import ru.strelchm.techarm.service.DataService;
import ru.strelchm.techarm.service.UserService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.strelchm.techarm.config.OpenApiConfig.SUCCESS_MESSAGE_FIELD;

@RestController
@RequestMapping("/api/data")
@Validated
@Tag(name = "/api/data", description = "Data operations")
public class DataController extends ParentController {
    private final DataService dataService;
    private final DataMapper dataMapper;

    @Autowired
    public DataController(DataService dataService, UserService userService, DataMapper dataMapper) {
        super(userService);
        this.dataService = dataService;
        this.dataMapper = dataMapper;
    }

    @GetMapping
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get all data", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataCreateListDto.class))
    ))
    public DataListDtoResponse getAllDatas(
            @RequestParam(value = "deviceId") String deviceId,
            @RequestParam(value = "functionKey") String functionKey,
            @RequestParam(value = "start", required = false) Long start,
            @RequestParam(value = "end", required = false) Long end
    ) {
        return new DataListDtoResponse(dataService.getAll(getDataSpecification(deviceId, functionKey, start, end))
                .stream()
                .map(dataMapper::toDataDto)
                .collect(Collectors.toList()));
    }

    Specification<Data> getDataSpecification(String deviceId, String functionKey, Long start, Long end) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (deviceId != null) {
                Join<Data, Device> deviceJoin = root.join("device");
                predicates.add(cb.equal(deviceJoin.get("id"), UUID.fromString(deviceId)));
            }
            if (functionKey != null) {
                predicates.add(cb.equal(root.get("functionKey"), functionKey));
            }
            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("processedTime"), new Date(start)));
            }
            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("processedTime"), new Date(end)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

//    @GetMapping("/stat")
//    @ApiResponse(description = "Successful Operation", responseCode = "200")
//    @Operation(
//            summary = "Get statistics", responses = @ApiResponse(
//            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
//            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatasStatDto.class))
//    ))
//    public DatasStatDto getStatistics(@RequestParam(value = "start", required = false) Long start,
//            @RequestParam(value = "end", required = false) Long end,
//            @RequestParam(value = "deviceIds", required = false) List<UUID> deviceId) {
//        Date startDate = start == null ? null : new Date(start);
//        Date endDate = end == null ? null : new Date(end);
//        List<DataStatDto> statistics = dataService.getStatistics(startDate, endDate, deviceId);
//        return new DatasStatDto(startDate, endDate, statistics);
//    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public void createData(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody DataCreateListDto dto,
                               @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        dataService.add(dto);
    }
}
