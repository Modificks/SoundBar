package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProperties;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Formats.DateFormatter;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import Web.Player.SoundBar.Services.RefreshTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserServiceImpl userServiceImpl;

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepo refreshTokenRepo;

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {

                String refreshToken = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithmForRefreshToken = Algorithm.HMAC256(jwtProperties.getJwtRefreshSecret().getBytes());
                Algorithm algorithmForAccessToken = Algorithm.HMAC256(jwtProperties.getJwtAccessSecret().getBytes());

                JWTVerifier verifier = JWT.require(algorithmForRefreshToken).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String email = decodedJWT.getSubject();

                User user = userServiceImpl.getUser(email);

                Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + ((long)jwtProperties.getAccessExpiration() * 1000));
                Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + ((long)jwtProperties.getRefreshExpiration() * 1000));

                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(accessTokenExpirationDate)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getUserRoles().stream()
                                .map(role -> role.getRoleName().name())
                                .collect(Collectors.toList()))
                        .sign(algorithmForAccessToken);

                String accessTokenExpirationTime = DateFormatter.formatTime(LocalDateTime.ofInstant(accessTokenExpirationDate.toInstant(), ZoneId.systemDefault()));
                String refreshTokenExpirationTime = DateFormatter.formatTime(LocalDateTime.ofInstant(refreshTokenExpirationDate.toInstant(), ZoneId.systemDefault()));
                RefreshToken refreshTokenEntity1 = new RefreshToken();

                RefreshToken refreshTokenEntity = refreshTokenRepo.findByToken(refreshToken);
                refreshTokenEntity.setIsUsed(Boolean.TRUE);

                checkAndDeleteToken(refreshToken);

                refreshTokenEntity1.setIsUsed(Boolean.FALSE);
                refreshTokenEntity1.setToken(refreshToken);
                refreshTokenEntity1.setUser(user);
                refreshTokenRepo.save(refreshTokenEntity1);

                Map<String, String> tokens = new HashMap<>();

                tokens.put("access_token", accessToken);
                tokens.put("access_token_expires", accessTokenExpirationTime);
                tokens.put("refresh_token", refreshToken);
                tokens.put("refresh_token_expires", refreshTokenExpirationTime);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                //TODO: handle scenario if smth is incorrect

                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();

                error.put("error_message", exception.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            // TODO: throw more specified exception
            throw new RuntimeException("refresh token is missing");
        }
    }

    @Override
    public void checkAndDeleteToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);

        if (Boolean.TRUE.equals(refreshToken.getIsUsed())) {
            refreshTokenRepo.deleteByToken(token);
        }
    }
}