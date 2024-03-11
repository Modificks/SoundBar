package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.Entities.Song;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PlayListDTO {

    private String name;
    private UserDTO user;
    private Set<Song> playListsMusic;
}