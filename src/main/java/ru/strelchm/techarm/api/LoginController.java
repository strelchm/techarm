package ru.strelchm.techarm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.strelchm.techarm.domain.User;
import ru.strelchm.techarm.domain.UserStatus;
import ru.strelchm.techarm.domain.repo.UserRepository;
import ru.strelchm.techarm.dto.LoginRequestDto;
import ru.strelchm.techarm.dto.TokenResponseDto;
import ru.strelchm.techarm.exception.AccessDeniedException;
import ru.strelchm.techarm.service.JwtTokenService;
import ru.strelchm.techarm.service.UserService;

import javax.validation.constraints.NotNull;

@RestController
//@Api("REST controller 4 log in")
@RequestMapping("/api/login")
@Validated
public class LoginController extends ParentController {
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder encoder;
//    private final BlockedUserRepository blockedUserRepository;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository, JwtTokenService tokenService,
                           PasswordEncoder encoder) { //, BlockedUserRepository blockedUserRepository) {
        super(userService);
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.encoder = encoder;
//        this.blockedUserRepository = blockedUserRepository;
    }

    @PostMapping
    public TokenResponseDto login(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody LoginRequestDto dto) {
        User user = userRepository.findByLogin(dto.getLogin()).orElseThrow(() -> new AccessDeniedException("Wrong login"));
        if (user.getStatus() == UserStatus.GLOBAL_BLOCKED) {
            throw new AccessDeniedException("User is blocked");
        }
//        if (blockedUserRepository.findById(user.getId()).isPresent()) {
//            blockedUserRepository.deleteById(user.getId());
//        }
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Wrong password");
        }
        return new TokenResponseDto(tokenService.generateToken(user));
    }
}
