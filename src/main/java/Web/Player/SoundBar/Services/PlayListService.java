package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.ViewLayers.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import java.util.List;

public interface PlayListService extends GeneralInterface<Long> {

    PlayList createPlayList(PlayListDTO playListDTO);

    List<PlayList> getAllPlayLists();
}