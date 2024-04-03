package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.Criterias.SongPage;
import Web.Player.SoundBar.Domains.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SongService {

    List<Song> addSong(List<SongDTO> listOfSongBaseDTO, List<MultipartFile> multipartFiles);

    List<Song> getAllSongs();

    Page<Song> getSongs(SongPage songPage, SongSearchCriteria songSearchCriteria);
}