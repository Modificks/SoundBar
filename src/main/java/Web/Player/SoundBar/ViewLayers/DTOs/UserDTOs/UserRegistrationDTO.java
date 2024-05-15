package Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs;

import Web.Player.SoundBar.Annotations.Nickname;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserRegistrationDTO extends LoginDTO {

    @Nickname(message = "Invalid nickname")
    private String nickname;
    private Set<UserRole> userRoles;
    private Boolean isArtist;
}