package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.DTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SongService {

    List<Song> addSong(List<SongBaseDTO> listOfSongBaseDTO, List<MultipartFile> multipartFiles);

    List<Song> getAllSongs();
}