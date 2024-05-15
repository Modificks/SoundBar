package Web.Player.SoundBar.ViewLayers.DTOs;

import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PlayListDTO {

    private String name;
    private Set<SongDTO> playListsMusic;
}