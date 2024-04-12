package Web.Player.SoundBar.APIs.ArtistController;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongStatisticDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Services.Impl.SongServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("SoundBar/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final SongServiceImpl songServiceImpl;

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/statistic/{id}")
    public SongStatisticDTO[] getStatistic(@PathVariable String id) {
        Long artistId = Long.parseLong(id);

        return songServiceImpl.getSongStatistic(artistId);
    }

    @GetMapping("/salary/{id}")
    public BigDecimal getSalary(@PathVariable String id) {
        Long artistId = Long.parseLong(id);

        return userServiceImpl.getSalary(artistId);
    }

    @PostMapping(value = "/upload-song", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Song> uploadSong(@RequestPart(value = "songBaseDTO") List<SongDTO> songBaseDTO,
                                 @RequestPart(value = "file") List<MultipartFile> files) {
        return songServiceImpl.addSong(songBaseDTO, files);
    }

    @DeleteMapping("/delete-song")
    public void deleteFile(@RequestBody SongBaseDTO songBaseDTO) {
        songServiceImpl.deleteSong(songBaseDTO);
    }
}