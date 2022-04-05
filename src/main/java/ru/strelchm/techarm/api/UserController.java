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
import ru.strelchm.techarm.dto.IdDto;
import ru.strelchm.techarm.dto.UserContext;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.dto.UserListDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static ru.strelchm.techarm.config.OpenApiConfig.SUCCESS_MESSAGE_FIELD;

@RestController
//@Api("REST controller 4 user operations")
@RequestMapping("/api/users")
@Validated
@Tag(name = "/api/users", description = "User operations")
//@PreAuthorize("hasAnyRole()") todo - держать открытой регистрацию
public class UserController extends ParentController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping
    @ApiResponse(description = "Successful Operation", responseCode = "200")
    @Operation(
            summary = "Get all users", responses = @ApiResponse(
            responseCode = "200", description = SUCCESS_MESSAGE_FIELD,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserListDto.class))
    ))
    public UserListDto getAllUsers() {
        return new UserListDto(userService.getAll());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping("/self")
    public UserDto getSelf(@ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return userService.getSelf(userContext.getUser().get());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured({"ROLE_CLIENT", "ROLE_ADMIN"})
    public IdDto createUser(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto) {
        return new IdDto(userService.add(dto));
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto,
                             @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        UserDto userDto = userService.getById(id);
        if (!userDto.getLogin().equals(dto.getLogin())) {
            userDto.setLogin(dto.getLogin());
        }
        if (!userDto.getStatus().equals(dto.getStatus())) {
            userDto.setStatus(dto.getStatus());
        }
        return userService.edit(userDto, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        userService.delete(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }

    @PostMapping("/{id}/block")
    @ResponseStatus(value = HttpStatus.OK)
    public void blockUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        userService.blockUser(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }

    @PostMapping("/{id}/unblock")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unblockUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                          @ModelAttribute(USER_CONTEXT) @Parameter(hidden = true)  UserContext userContext) {
        userService.unblockUser(id, userContext.getUser().orElseThrow(AccessDeniedException::new));
    }
}
