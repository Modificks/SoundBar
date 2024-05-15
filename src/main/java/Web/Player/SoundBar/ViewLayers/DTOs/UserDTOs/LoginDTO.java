package Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "This field cannot be empty")
    private String email;

    @NotBlank(message = "This field cannot be empty")
    private String password;
}