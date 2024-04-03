package Web.Player.SoundBar.Domains.DTOs;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.Song;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
public class PlayerPageDTO {
    private List<PlayList> playLists;
    private List<Song> songs;
    private Page<Song> songPage;
}