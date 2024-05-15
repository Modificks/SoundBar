package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.ViewLayers.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongStatisticDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SongService extends GeneralInterface<Long> {

    List<Song> addSong(List<SongDTO> listOfSongDTO, List<MultipartFile> multipartFiles);

    Song listenToSong(Long songId);

    Page<Song> getSongs(SongSearchCriteria songSearchCriteria, Pageable songPage);

    SongStatisticDTO[] getSongStatistic(Long artistId);

    void removeSongFromPlayList(Long playListId, Long songId);

    void addSongToPlayList(Long playListId, Long songId);

    List<Song> getAllSongs();
}