package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.DTOs.PlayerPageDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Services.Impl.PlayListServiceImp;
import Web.Player.SoundBar.Services.Impl.SongServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/SoundBar/player")
public class UserController {

    private final PlayListServiceImp playListServiceImp;

    private final SongServiceImpl songServiceImpl;

    @GetMapping
    public PlayerPageDTO getPlayerPage() {
        PlayerPageDTO playerPageDTO = new PlayerPageDTO();

        playerPageDTO.setPlayLists(playListServiceImp.getAllPlayLists());
        playerPageDTO.setSongs(songServiceImpl.getAllSongs());
        return playerPageDTO;
    }

    @PostMapping("/create_play_list")
    public PlayList createPlayList(@RequestBody PlayListDTO playListDTO) {
        return playListServiceImp.createPlayList(playListDTO);
    }

    @PatchMapping("/delete-song-from-playList")
    public void deleteSongFromPlayList(@RequestParam("playListId") Long playListId,
                                       @RequestParam("songId") Long songId) {
        playListServiceImp.removeSongFromPlayList(playListId, songId);
    }

    @PatchMapping("/player/add-song-to-playList")
    public void addSongToPlayList(@RequestParam("playListId") Long playListId,
                                  @RequestParam("songId") Long songId) {
        playListServiceImp.addSongToPlayList(playListId, songId);
    }

    @DeleteMapping("/player/delete-play-list")
    public void deletePlayList(@RequestBody PlayListDTO playListDTO) {
        playListServiceImp.deletePlayList(playListDTO);
    }
}