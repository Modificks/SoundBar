package Web.Player.SoundBar.Configs.SecurityConfig.Filters;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProperties;
import Web.Player.SoundBar.Domains.DTOs.RefreshTokenDTO;
import Web.Player.SoundBar.Domains.Mapper.RefreshTokenMapper;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Formats.DateFormatter;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserServiceImpl userServiceImpl;

    private final UserMapper userMapper;

    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();

        String email = user.getUsername();
        Web.Player.SoundBar.Domains.Entities.User userEntity = userServiceImpl.getUser(email);

        Algorithm algorithmForAccessToken = Algorithm.HMAC256(jwtProperties.getJwtAccessSecret().getBytes());
        Algorithm algorithmForRefreshToken = Algorithm.HMAC256(jwtProperties.getJwtRefreshSecret().getBytes());

        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) jwtProperties.getAccessExpiration() * 1000));
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) jwtProperties.getRefreshExpiration() * 1000));

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(accessTokenExpirationDate)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithmForAccessToken);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(refreshTokenExpirationDate)
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithmForRefreshToken);

        String accessTokenExpirationTime =
                DateFormatter.formatTime(LocalDateTime.ofInstant(accessTokenExpirationDate.toInstant(), ZoneId.of("UTC")));
        String refreshTokenExpirationTime =
                DateFormatter.formatTime(LocalDateTime.ofInstant(refreshTokenExpirationDate.toInstant(), ZoneId.of("UTC")));

        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();

        refreshTokenDTO.setToken(refreshToken);
        refreshTokenDTO.setIsUsed(Boolean.FALSE);
        refreshTokenDTO.setUser(userMapper.toDto(userEntity));

        refreshTokenRepo.save(refreshTokenMapper.toEntity(refreshTokenDTO));

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        tokens.put("access_token_expires", accessTokenExpirationTime);
        tokens.put("refresh_token", refreshToken);
        tokens.put("refresh_token_expires", refreshTokenExpirationTime);

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}