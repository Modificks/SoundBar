package Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs;

import Web.Player.SoundBar.Domains.Enums.SongGenres;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDTO extends SongBaseDTO {

    private String title;
    private SongGenres genre;
}