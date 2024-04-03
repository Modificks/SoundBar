package Web.Player.SoundBar.Domains.Criterias;

import Web.Player.SoundBar.Enums.SongGenres;
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