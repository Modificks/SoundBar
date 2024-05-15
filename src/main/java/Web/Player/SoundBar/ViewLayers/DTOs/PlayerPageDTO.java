package Web.Player.SoundBar.ViewLayers.DTOs;

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
    private Page<Song> songPage;
    private List<Song> songs;
}