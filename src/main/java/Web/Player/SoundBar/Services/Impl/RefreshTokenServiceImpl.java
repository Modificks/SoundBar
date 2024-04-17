package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Configs.SecurityConfig.JwtProvider;
import Web.Player.SoundBar.Domains.DTOs.TokenDTO;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Formats.DateFormatter;
import Web.Player.SoundBar.Repositories.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl {

    private final JwtProvider jwtProvider;

    private final RefreshTokenRepo refreshTokenRepo;

    public TokenDTO generateTokens(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshTokenString = jwtProvider.generateRefreshToken(user);

        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) jwtProvider.getAccessExpiration() * 1000));
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + ((long) jwtProvider.getRefreshExpiration() * 1000));

        String accessTokenExpirationTime =
                DateFormatter.formatTime(LocalDateTime.ofInstant(accessTokenExpirationDate.toInstant(), ZoneId.of("UTC")));
        String refreshTokenExpirationTime =
                DateFormatter.formatTime(LocalDateTime.ofInstant(refreshTokenExpirationDate.toInstant(), ZoneId.of("UTC")));

        RefreshToken newRefreshToken = new RefreshToken();

        newRefreshToken.setUser(user);
        newRefreshToken.setIsUsed(false);
        newRefreshToken.setToken(refreshTokenString);

        refreshTokenRepo.save(newRefreshToken);

        return new TokenDTO(accessToken, accessTokenExpirationTime, refreshTokenString, refreshTokenExpirationTime);
    }
}