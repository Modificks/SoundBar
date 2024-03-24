package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO extends UserBaseDTO {

    private String password;
}