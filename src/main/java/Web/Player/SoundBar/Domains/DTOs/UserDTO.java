package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserDTO extends UserAuthDTO {

    private Set<PlayList> playList;
    private RefreshToken refreshToken;
}