package Web.Player.SoundBar.Configs.SecurityConfig;

import Web.Player.SoundBar.Configs.SecurityConfig.Filters.AccessTokenFilter;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userServiceImpl;

    private final JwtProvider jwtProvider;

    public SecurityConfig(JwtProvider jwtProvider, @Lazy UserServiceImpl userServiceImpl) {
        this.jwtProvider = jwtProvider;
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public AccessTokenFilter accessTokenFilter() {
        return new AccessTokenFilter(jwtProvider, userServiceImpl);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/SoundBar", "/SoundBar/login", "/SoundBar/logout", "/SoundBar/logout-all", "/SoundBar/registration", "/SoundBar/refresh-token").permitAll()

                .antMatchers("/SoundBar/player/**").hasAnyAuthority("USER", "ARTIST", "ADMIN", "SUPER_ADMIN")
                .antMatchers("SoundBar/artist/**").hasAnyAuthority("ARTIST")
                .antMatchers("/SoundBar/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}