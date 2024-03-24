package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {

    private String token;
    private Boolean isUsed;
    private UserDTO user;
}