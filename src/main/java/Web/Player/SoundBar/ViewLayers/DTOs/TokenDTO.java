package Web.Player.SoundBar.ViewLayers.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {

    private String accessToken;
    private String accessTokenExpiration;
    private String refreshToken;
    private String refreshTokenExpiration;
}