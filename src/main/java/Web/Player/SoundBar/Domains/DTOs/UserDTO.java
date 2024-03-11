package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Annotations.Nickname;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @Email(message = "Invalid email")
    private String email;

    @Nickname(message = "Invalid nickname")
    private String nickname;

    private String password;
    private Set<UserRole> userRoles;
    private Set<PlayListDTO> playList;
    private RefreshToken refreshToken;
    private Boolean isArtist;

}