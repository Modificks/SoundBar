package Web.Player.SoundBar.Configs.SecurityConfig.Filters;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
// TODO: use another type of filter to validate only requests which are covered by the security configs and should have authentication
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO: remove those routes from the Security configs and here
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {

                    String token = authorizationHeader.substring("Bearer ".length());

                    Algorithm algorithmForAccessToken = Algorithm.HMAC256(jwtProperties.getJwtAccessSecret().getBytes());

                    JWTVerifier verifier = JWT.require(algorithmForAccessToken).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String email = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    stream(roles).forEach(role ->
                            authorities.add(new SimpleGrantedAuthority(role))
                    );

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);

                } catch (Exception exception) {
                    //TODO: handle scenario if smth is incorrect

                    if (!response.isCommitted()) {
                        response.setHeader("error", exception.getMessage());
                        response.setStatus(FORBIDDEN.value());

                        Map<String, String> error = new HashMap<>();

                        error.put("error_message", exception.getMessage());

                        response.setStatus(FORBIDDEN.value());
                        response.setContentType(APPLICATION_JSON_VALUE);

                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}