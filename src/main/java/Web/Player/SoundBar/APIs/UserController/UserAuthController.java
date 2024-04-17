package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProvider;
import Web.Player.SoundBar.Domains.DTOs.TokenDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.LoginDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.Impl.RefreshTokenServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Validated
@RestController
@Transactional
@RequestMapping("/SoundBar")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserServiceImpl userServiceImpl;

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userServiceImpl.saveUser(userMapper.toEntity(userRegistrationDTO), userRegistrationDTO.getIsArtist());

        return "Click on login";
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwtProvider.setAuthentication(authentication);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User existingUser = userRepo.findByEmail(user.getUsername());

        return refreshTokenServiceImpl.generateTokens(existingUser);
    }



    @PostMapping("/logout")
    public String logout(@RequestBody TokenDTO tokenDTO) {
        String refreshTokenString = tokenDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepo.findByToken(tokenDTO.getRefreshToken());
        refreshToken.setIsUsed(Boolean.TRUE);

        if (jwtProvider.validateRefreshToken(refreshTokenString) && refreshToken.getIsUsed().equals(Boolean.TRUE)) {
            refreshTokenRepo.deleteByToken(tokenDTO.getRefreshToken());
            return "Successfully logout from account on this device";
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/logout-all")
    public String logoutAll(@RequestBody TokenDTO tokenDTO) {
        String refreshTokenString = tokenDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepo.findByToken(tokenDTO.getRefreshToken());

        if (jwtProvider.validateRefreshToken(refreshTokenString) && refreshToken != null) {
            refreshTokenRepo.deleteAllByUserEmail(jwtProvider.getUserIdFromRefreshToken(refreshTokenString));
            return "Successfully logout from account from all devices";
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/refresh-token")
    public TokenDTO refreshToken(@RequestBody TokenDTO tokenDTO) {
        String refreshTokenString = tokenDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepo.findByToken(tokenDTO.getRefreshToken());

        if (jwtProvider.validateRefreshToken(refreshTokenString) && refreshToken != null) {

            refreshToken.setIsUsed(Boolean.TRUE);

            refreshTokenRepo.delete(refreshToken);

            User userEnt = userRepo.findByEmail(jwtProvider.getUserIdFromRefreshToken(refreshTokenString));

            return refreshTokenServiceImpl.generateTokens(userEnt);
        }
        throw new BadCredentialsException("Bad credits");
    }
}