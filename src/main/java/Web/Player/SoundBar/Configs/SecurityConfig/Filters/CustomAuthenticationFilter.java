package Web.Player.SoundBar.Configs.SecurityConfig.Filters;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProperties;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();

        String email = user.getUsername();
        Web.Player.SoundBar.Domains.Entities.User userEntity = userServiceImpl.getUser(email);

        Algorithm algorithmForAccessToken = Algorithm.HMAC256(jwtProperties.getJwtAccessSecret().getBytes());
        Algorithm algorithmForRefreshToken = Algorithm.HMAC256(jwtProperties.getJwtRefreshSecret().getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ((long)jwtProperties.getAccessExpiration() * 1000)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithmForAccessToken);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ((long)jwtProperties.getRefreshExpiration() * 1000)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithmForRefreshToken);

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsed(false);
        refreshTokenEntity.setUser(userEntity);

        refreshTokenRepo.save(refreshTokenEntity);

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);

        //TODO: Return expiration time in the response to follow auth conversions (Just suggestion)
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}