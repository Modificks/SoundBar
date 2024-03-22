package Web.Player.SoundBar.Domains.DTOs;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SongDTO extends SongBaseDTO {

    private Long id;
    private String url;
    private Set<PlayListDTO> playListsMusic;
}