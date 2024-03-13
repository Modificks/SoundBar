package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PlayListDTO {

    private String name;
    private UserBaseDTO userBaseDTO;
    private Set<Song> playListsMusic;
}