package ru.strelchm.techarm.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.strelchm.techarm.dto.UserDto;
import ru.strelchm.techarm.service.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.strelchm.techarm.service.impl.JwtTokenServiceImpl.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService tokenService;
//    private final BlockedUserRepository blockedUserRepository;

    public JwtAuthenticationFilter(JwtTokenService tokenService) { // , BlockedUserRepository blockedUserRepository) {
        this.tokenService = tokenService;
//        this.blockedUserRepository = blockedUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = authorizationHeader.replace(TOKEN_PREFIX, "");

        try {
            UserDto userPrincipal = tokenService.parseToken(token);

//        if(blockedUserRepository.findById(userPrincipal.getId()).isPresent()) {
//            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
//            httpServletResponse.getWriter().write("User is blocked");
//            return;
//        }

            SecurityContextHolder.getContext().setAuthentication(createToken(userPrincipal));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException e) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write("Token expired");
        }
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    private UsernamePasswordAuthenticationToken createToken(UserDto userPrincipal) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (userPrincipal.getUserAppRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userPrincipal.getUserAppRole().name()));
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
    }
}
