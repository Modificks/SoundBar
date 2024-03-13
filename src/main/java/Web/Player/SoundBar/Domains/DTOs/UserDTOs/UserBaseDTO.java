package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;

@Getter
@Setter
public class UserBaseDTO {

    private Long id;
    @Email(message = "Invalid email")
    private String email;
}