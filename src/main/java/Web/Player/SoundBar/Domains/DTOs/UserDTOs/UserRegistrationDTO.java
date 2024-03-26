package Web.Player.SoundBar.Domains.DTOs.UserDTOs;

import Web.Player.SoundBar.Annotations.Nickname;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO extends UserAuthDTO {

    @Nickname(message = "Invalid nickname")
    private String nickname;
    private Boolean isArtist;
}