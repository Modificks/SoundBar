package Web.Player.SoundBar.APIs.PlayerPageControllers;

import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Services.SongService;
import Web.Player.SoundBar.Services.UserService;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongStatisticDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/sound-bar")
@RequiredArgsConstructor
public class SongController {

    private final UserService userService;
    private final SongService songService;

    @GetMapping("/artist/{id}/statistic")
    public SongStatisticDTO[] getStatistic(@PathVariable String id) {
        Long artistId = Long.parseLong(id);

        return songService.getSongStatistic(artistId);
    }

    @GetMapping("/artist/salary")
    public BigDecimal getSalary() {
        return userService.getSalary();
    }

    @PostMapping(value = "/artist/upload-song", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Song> uploadSong(@RequestPart(value = "songDTO") List<SongDTO> songDTO,
                                 @RequestPart(value = "file") List<MultipartFile> files) {
        return songService.addSong(songDTO, files);
    }

    @PatchMapping("/player/song/{id}/listen")
    public Song listenToSong(@PathVariable String id) {
        Long songId = Long.parseLong(id);

        return songService.listenToSong(songId);
    }

    @PatchMapping("/player/add-song/{songId}/{playListId}")
    public void addSongToPlayList(@PathVariable String songId,
                                  @PathVariable String playListId) {
        Long plId = Long.parseLong(playListId);
        Long sId = Long.parseLong(songId);

        songService.addSongToPlayList(plId, sId);
    }

    @PatchMapping("/player/remove-song/{songId}/{playListId}")
    public void deleteSongFromPlayList(@PathVariable String songId,
                                       @PathVariable String playListId) {
        Long plId = Long.parseLong(playListId);
        Long sId = Long.parseLong(songId);

        songService.removeSongFromPlayList(plId, sId);
    }

    @DeleteMapping(value = {
            "/admin/song/delete/{id}",
            "/artist/song/delete/{id}"
    })
    public void deleteSong(@PathVariable String id) {
        Long songId = Long.parseLong(id);

        songService.delete(songId);
    }
}