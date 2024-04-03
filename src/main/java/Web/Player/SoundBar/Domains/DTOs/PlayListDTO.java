package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PlayListDTO {

    private String name;
    private UserBaseDTO userBaseDTO;
    private Set<SongDTO> playListsMusic;
}