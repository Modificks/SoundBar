package Web.Player.SoundBar.ViewLayers.Criterias;

import Web.Player.SoundBar.Domains.Enums.SongGenres;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SongSearchCriteria {

    private String title;
    private Set<SongGenres> genre;
    private String artistNickname;
}