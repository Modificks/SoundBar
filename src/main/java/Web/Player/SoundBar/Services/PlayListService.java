package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import java.util.List;

public interface PlayListService {

    PlayList createPlayList(PlayListDTO playListDTO);

    void deletePlayList(PlayListDTO playListDTO);

    void removeSongFromPlayList(Long playListId, Long songId);

    void addSongToPlayList(Long playListId, Long songId);

    List<PlayList> getAllPlayLists();
}