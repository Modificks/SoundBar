package Web.Player.SoundBar.Configs.SecurityConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {

    @Value("${jwt.access-secret}")
    private String jwtAccessSecret;

    @Value("${jwt.refresh-secret}")
    private String jwtRefreshSecret;

    @Value("${security.oauth2.client.access-token-validity-seconds}")
    private short accessExpiration;

    @Value("${security.oauth2.client.refresh-token-validity-seconds}")
    private short refreshExpiration;
}