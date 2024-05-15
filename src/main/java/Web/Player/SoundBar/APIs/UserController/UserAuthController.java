package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProvider;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import Web.Player.SoundBar.Services.RefreshTokenService;
import Web.Player.SoundBar.Services.UserService;
import Web.Player.SoundBar.ViewLayers.DTOs.TokenDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.LoginDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.UserRegistrationDTO;
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
@RequestMapping("/sound-bar")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.register(userRegistrationDTO, userRegistrationDTO.getIsArtist());

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

        User userEnt = userService.findByEmail(user.getUsername());

        return refreshTokenService.generateTokens(userEnt);
    }

    @PostMapping("/player/logout")
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

    @PostMapping("/player/logout-all")
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

            User userEnt = userService.findByEmail(jwtProvider.getUserIdFromRefreshToken(refreshTokenString));

            return refreshTokenService.generateTokens(userEnt);
        }
        throw new BadCredentialsException("Bad credits");
    }
}