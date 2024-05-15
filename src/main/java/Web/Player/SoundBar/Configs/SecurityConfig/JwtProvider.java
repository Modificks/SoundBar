package Web.Player.SoundBar.Configs.SecurityConfig;

import Web.Player.SoundBar.Domains.Entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@Slf4j
public class JwtProvider {

    private static final String ISSUER = "SoundBar";

    private HttpServletRequest request;

    private Algorithm accessAlgorithm;

    private Algorithm refreshAlgorithm;

    private JWTVerifier accessTokenVerifier;

    private JWTVerifier refreshTokenVerifier;

    private Authentication authentication;

    @Value("${security.oauth2.client.access-token-validity-seconds}")
    private short accessExpiration;

    @Value("${security.oauth2.client.refresh-token-validity-seconds}")
    private short refreshExpiration;

    public JwtProvider(@Value("${jwt.access-secret}") String jwtAccessSecret,
                       @Value("${jwt.refresh-secret}") String jwtRefreshSecret) {

        accessAlgorithm = Algorithm.HMAC256(jwtAccessSecret.getBytes());
        refreshAlgorithm = Algorithm.HMAC256(jwtRefreshSecret.getBytes());

        accessTokenVerifier = JWT.require(accessAlgorithm)
                .withIssuer(ISSUER)
                .build();

        refreshTokenVerifier = JWT.require(refreshAlgorithm)
                .withIssuer(ISSUER)
                .build();
    }

    public String generateAccessToken(User user) {
        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) accessExpiration * 1000));

        org.springframework.security.core.userdetails.User user1 =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getEmail())
                .withClaim("roles", user1.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withExpiresAt(accessTokenExpirationDate)
                .sign(accessAlgorithm);
    }

    public String generateRefreshToken(User user) {
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) refreshExpiration * 1000));

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(refreshTokenExpirationDate)
                .sign(refreshAlgorithm);
    }

    private Optional<DecodedJWT> decodedAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException exception) {
            log.warn("Invalid access token: ", exception);
        }
        return Optional.empty();
    }

    private Optional<DecodedJWT> decodedRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException exception) {
            log.warn("Invalid refresh token: ", exception);
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(String token) {
        return decodedAccessToken(token).isPresent();
    }

    public boolean validateRefreshToken(String token) {
        return decodedRefreshToken(token).isPresent();
    }

    public String getUserIdFromAccessToken(String token) {
        return decodedAccessToken(token).get().getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        return decodedRefreshToken(token).get().getSubject();
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}