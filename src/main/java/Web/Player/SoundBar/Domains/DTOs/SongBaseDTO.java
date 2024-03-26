package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Enums.SongGenres;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongBaseDTO {

    private Long id;
    private String title;
    private SongGenres genre;
    private String url;
    private Long artistId;
}