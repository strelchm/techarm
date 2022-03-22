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
import ru.strelchm.techarm.dto.DeviceDto;
import ru.strelchm.techarm.dto.DeviceListDto;
import ru.strelchm.techarm.dto.IdDto;
import ru.strelchm.techarm.dto.UserContext;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.mapping.DeviceMapper;
import ru.strelchm.techarm.service.DeviceService;
import ru.strelchm.techarm.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.strelchm.techarm.config.OpenApiConfig.SUCCESS_MESSAGE_FIELD;

@RestController
@RequestMapping("/api/devices")
@Validated
@Tag(name = "/api/devices", description = "Device operations")
public class DeviceController extends ParentController {
    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;

    @Autowired
    public DeviceController(DeviceService deviceService, UserService userService, DeviceMapper deviceMapper) {
        super(userService);
        this.deviceService = deviceService;
        this.deviceMapper = deviceMapper;
    }

    @GetMapping
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get all users", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceListDto.class))
    ))
    public DeviceListDto getAllDevices() {
        return new DeviceListDto(deviceService.getAll().stream().map(deviceMapper::toDeviceDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public DeviceDto getDeviceById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return deviceMapper.toDeviceDto(deviceService.getById(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public IdDto createDevice(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody DeviceDto dto,
                              @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true) UserContext userContext) {
        return new IdDto(deviceService.add(deviceMapper.fromDeviceDto(dto), userContext.getUser().get()));
    }

    @PatchMapping("/{id}")
    public DeviceDto patchDevice(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                 @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody DeviceDto dto,
                                 @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        return deviceMapper.toDeviceDto(
                deviceService.edit(deviceMapper.fromDeviceDto(dto), userContext.getUser().orElseThrow(AccessDeniedException::new))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDevice(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        deviceService.delete(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }
}
