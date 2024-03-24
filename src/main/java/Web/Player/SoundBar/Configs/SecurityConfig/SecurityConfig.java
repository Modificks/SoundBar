package Web.Player.SoundBar.Configs.SecurityConfig;

import Web.Player.SoundBar.Configs.SecurityConfig.Filters.CustomAuthenticationFilter;
import Web.Player.SoundBar.Configs.SecurityConfig.Filters.CustomAuthorizationFilter;
import Web.Player.SoundBar.Domains.Mapper.RefreshTokenMapper;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.DELETE;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtProperties jwtProperties;

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserServiceImpl userServiceImpl;

    private final UserMapper userMapper;

    private final RefreshTokenMapper refreshTokenMapper;


    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean(), jwtProperties, refreshTokenRepo, userServiceImpl, userMapper, refreshTokenMapper);

        customAuthenticationFilter.setFilterProcessesUrl("/SoundBar/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/SoundBar", "/SoundBar/login", "/SoundBar/registration", "/SoundBar/player/refresh").permitAll();

        http.authorizeRequests().antMatchers(GET, "/SoundBar/player/**").hasAnyAuthority("USER", "ARTIST", "ADMIN", "SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/SoundBar/player/**").hasAnyAuthority("USER", "ARTIST", "ADMIN", "SUPER_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/SoundBar/player/**").hasAnyAuthority("USER", "ARTIST", "ADMIN", "SUPER_ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/SoundBar/player/**").hasAnyAuthority("USER", "ARTIST", "ADMIN", "SUPER_ADMIN");

        http.authorizeRequests().antMatchers(GET, "SoundBar/artist/**").hasAnyAuthority("ARTIST");
        http.authorizeRequests().antMatchers(POST, "SoundBar/artist/**").hasAnyAuthority("ARTIST");
        http.authorizeRequests().antMatchers(DELETE, "SoundBar/artist/**").hasAnyAuthority("ARTIST");

        http.authorizeRequests().antMatchers(GET, "/SoundBar/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/SoundBar/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/SoundBar/admin/**").hasAnyAuthority("ADMIN");


        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class);
    }
}