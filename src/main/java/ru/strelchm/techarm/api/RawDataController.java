package ru.strelchm.techarm.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.strelchm.techarm.dto.*;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.mapping.RawDataMapper;
import ru.strelchm.techarm.service.RawDataService;
import ru.strelchm.techarm.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.strelchm.techarm.config.OpenApiConfig.SUCCESS_MESSAGE_FIELD;

@RestController
@RequestMapping("/api/raw_data")
@Validated
@Tag(name = "/api/raw_data", description = "RawData operations")
public class RawDataController extends ParentController {
    private final RawDataService rawDataService;
    private final RawDataMapper rawDataMapper;

    @Autowired
    public RawDataController(RawDataService rawDataService, UserService userService, RawDataMapper rawDataMapper) {
        super(userService);
        this.rawDataService = rawDataService;
        this.rawDataMapper = rawDataMapper;
    }

    @GetMapping
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get all raw data", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RawDataListDto.class))
    ))
    public RawDataListDto getAllRawDatas() {
        return new RawDataListDto(rawDataService.getAll().stream().map(rawDataMapper::toRawDataDto).collect(Collectors.toList()));
    }

    @GetMapping("/stat")
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get statistics", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RawDatasStatDto.class))
    ))
    public RawDatasStatDto getStatistics(@RequestParam(value = "start", required = false) Long start,
            @RequestParam(value = "end", required = false) Long end,
            @RequestParam(value = "deviceIds", required = false) List<UUID> deviceId) {
        Date startDate = start == null ? null : new Date(start);
        Date endDate = end == null ? null : new Date(end);
        List<RawDataStatDto> statistics = rawDataService.getStatistics(startDate, endDate, deviceId);
        return new RawDatasStatDto(startDate, endDate, statistics);
    }

    @GetMapping("/{id}")
    public RawDataDto getRawDataById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return rawDataMapper.toRawDataDto(rawDataService.getById(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public IdDto createRawData(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RawDataDto dto,
                               @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        return new IdDto(rawDataService.add(dto, userContext.getUser().get()));
    }

    @PostMapping("/{id}/status")
    public RawDataDto changeStatus(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                   @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RawDataStatusDto statusDto,
                                   @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        return rawDataMapper.toRawDataDto(rawDataService.setStatus(id, statusDto.getStatus(), new Date(statusDto.getProcessedTime())));
    }

    @PostMapping("/{id}/error")
    public RawDataDto changeError(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                  @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RawDataErrorDto errorDto,
                                  @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        return rawDataMapper.toRawDataDto(rawDataService.setError(id, errorDto.getErrorMessage(), new Date(errorDto.getProcessedTime())));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRawData(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                              @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        rawDataService.delete(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }
}
