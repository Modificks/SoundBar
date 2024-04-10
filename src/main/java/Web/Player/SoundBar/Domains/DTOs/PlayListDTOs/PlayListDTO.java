package Web.Player.SoundBar.Domains.DTOs.PlayListDTOs;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PlayListDTO extends PlayListBaseDTO {

    private UserBaseDTO userBaseDTO;
    private Set<SongDTO> playListsMusic;
}