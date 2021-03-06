package ru.strelchm.techarm.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.strelchm.techarm.domain.DeviceModel;
import ru.strelchm.techarm.dto.DeviceModelDto;
import ru.strelchm.techarm.dto.DeviceModelListDto;
import ru.strelchm.techarm.dto.IdDto;
import ru.strelchm.techarm.dto.UserContext;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.mapping.DeviceModelMapper;
import ru.strelchm.techarm.service.DeviceModelService;
import ru.strelchm.techarm.service.UserService;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.strelchm.techarm.config.OpenApiConfig.SUCCESS_MESSAGE_FIELD;

@RestController
@RequestMapping("/api/dev_models")
@Validated
@Tag(name = "/api/dev_models", description = "Device models operations")
public class DevModelController extends ParentController {
    private final DeviceModelService deviceModelService;
    private final DeviceModelMapper deviceModelMapper;

    @Autowired
    public DevModelController(DeviceModelService deviceModelService, UserService userService, DeviceModelMapper deviceModelMapper) {
        super(userService);
        this.deviceModelService = deviceModelService;
        this.deviceModelMapper = deviceModelMapper;
    }

    @GetMapping
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get all users", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceModelListDto.class))
    ))
    public DeviceModelListDto getAllDeviceModels(@RequestParam(value = "offset", required = false, defaultValue = DEFAULT_OFFSET) Integer offset,
                                                 @RequestParam(value = "count", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer count,
                                                 @RequestParam(value = "name", required = false) String name) {
        Page<DeviceModel> devices = deviceModelService.getAll(getDeviceModelSpecification(name), new OffsetBasedPageRequest(offset, count));
        return new DeviceModelListDto(
                devices.getContent().stream().map(deviceModelMapper::toDeviceModelDto).collect(Collectors.toList()),
                devices.getTotalElements()
        );
    }

    Specification<DeviceModel> getDeviceModelSpecification(String name) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @GetMapping("/{id}")
    public DeviceModelDto getDeviceModelById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return deviceModelMapper.toDeviceModelDto(deviceModelService.getById(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public IdDto createDeviceModel(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody DeviceModelDto dto,
                                   @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        return new IdDto(deviceModelService.add(deviceModelMapper.fromDeviceModelDto(dto), userContext.getUser().get()));
    }

    @PatchMapping("/{id}")
    public DeviceModelDto patchDeviceModel(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                           @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody DeviceModelDto dto,
                                           @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        return deviceModelMapper.toDeviceModelDto(
                deviceModelService.edit(deviceModelMapper.fromDeviceModelDto(dto), userContext.getUser().orElseThrow(AccessDeniedException::new))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDeviceModel(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                  @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        deviceModelService.delete(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }
}
