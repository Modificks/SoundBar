package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.DTOs.RefreshTokenDTO;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserDTO extends UserRegistrationDTO {

    private Set<UserRole> userRoles;
    private Set<PlayListDTO> playList;
    private RefreshTokenDTO refreshToken;
}