package ru.strelchm.techarm.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.strelchm.techarm.dto.UserContext;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.exception.BadRequestException;
import ru.strelchm.techarm.exception.NotFoundException;
import ru.strelchm.techarm.service.UserService;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ParentController {
    static final String USER_CONTEXT = "USER_CONTEXT";
    static final String NULL_ID_REQUEST_EXCEPTION = "Id cannot be null";
    static final String NULL_CREATE_OBJECT_REQUEST_EXCEPTION = "Instance that must be created not found in request body";
    static final String NULL_UPDATE_OBJECT_REQUEST_EXCEPTION = "Instance that must be updated not found in request body";
    static final String NULL_PATCH_OBJECT_REQUEST_EXCEPTION = "Instance that must be patch not found in request body";
    static final int DEFAULT_PAGE_SIZE = 100;

    protected final UserService userService;

    public ParentController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNotFoundExceptions(Exception ex) {
        return getResponseFromException(ex);
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public HashMap<String, String> handleAccessDeniedExceptions(Exception ex) {
        return getResponseFromException(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleBadRequestExceptions(Exception ex) {
        return getResponseFromException(ex);
    }

//    @NotNull
    private HashMap<String, String> getResponseFromException(Exception ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("error", ex.getClass().getSimpleName());
        ex.printStackTrace();
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleBadRequestExceptions(MethodArgumentNotValidException ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.put("error", ex.getClass().getSimpleName());
        ex.printStackTrace();
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public HashMap<String, String> handleIntervalServerExceptions(Exception ex) {
        return getResponseFromException(ex);
    }

    @ModelAttribute(USER_CONTEXT)
    public UserContext getControllerContext(Authentication authentication) {
        Optional<UUID> userId = Optional.ofNullable(authentication).map(auth -> (((UserDto) auth.getPrincipal()).getId()));
        return new UserContext(userId.map(userService::getById));
    }
}
