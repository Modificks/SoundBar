package Web.Player.SoundBar.APIs.PlayerPageControllers;

import Web.Player.SoundBar.Services.PlayListService;
import Web.Player.SoundBar.Services.SongService;
import Web.Player.SoundBar.ViewLayers.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.ViewLayers.DTOs.PlayerPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sound-bar/player")
public class PlayerPageController {

    private final PlayListService playListService;
    private final SongService songService;


    @GetMapping
    public PlayerPageDTO getPlayerPage() {
        PlayerPageDTO playerPageDTO = new PlayerPageDTO();

        playerPageDTO.setPlayLists(playListService.getAllPlayLists());
        playerPageDTO.setSongs(songService.getAllSongs());

        return playerPageDTO;
    }

    @GetMapping("/filter")
    public PlayerPageDTO filterSongs(Pageable pageable, SongSearchCriteria songSearchCriteria) {
        PlayerPageDTO playerPageDTO = new PlayerPageDTO();

        playerPageDTO.setPlayLists(playListService.getAllPlayLists());
        playerPageDTO.setSongPage(songService.getSongs(songSearchCriteria, pageable));

        return playerPageDTO;
    }
}