package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Enums.SongGenres;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SongDTO extends SongBaseDTO {

    private String title;
    private SongGenres genre;
    private Long artistId;
    private Set<PlayListDTO> playListsMusic;
}