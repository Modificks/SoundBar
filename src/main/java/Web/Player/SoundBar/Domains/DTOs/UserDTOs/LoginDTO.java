package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String email;
    private String password;
}