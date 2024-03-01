package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private String nickname;
    private String password;
    private Set<UserRole> userRoles;
    private Set<PlayList> playList;
}