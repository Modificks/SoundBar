package Web.Player.SoundBar.Domains.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {

    private String token;
    private Boolean isUsed;
    private UserDTO user;
}