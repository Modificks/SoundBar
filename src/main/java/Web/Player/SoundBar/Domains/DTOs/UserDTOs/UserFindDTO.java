package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListBaseDTO;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserFindDTO extends UserBaseDTO{
    private String nickname;
    private Set<UserRole> userRoles;
    private Set<PlayListBaseDTO> playList;
}