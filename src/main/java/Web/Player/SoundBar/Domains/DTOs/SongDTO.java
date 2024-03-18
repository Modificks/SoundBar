package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Enums.SongGenres;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SongDTO {

    private Long id;
    private String title;
    private SongGenres genre;
    private String url;
    private Artist artist;
    private Set<PlayListDTO> playListsMusic;
}