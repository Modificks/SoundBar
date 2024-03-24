package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;

public interface PlayListService {

    PlayList createPlayList(PlayListDTO playListDTO);

    void deletePlayList(PlayListDTO playListDTO);

    void removeSongFromPlayList(Long playListId, Long songId);

    void addSongToPlayList(Long playListId, Long songId);
}