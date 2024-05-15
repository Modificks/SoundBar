package Web.Player.SoundBar.APIs.PlayerPageControllers;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Services.PlayListService;
import Web.Player.SoundBar.ViewLayers.DTOs.PlayListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sound-bar/player")
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping("/create")
    public PlayList createPlayList(@RequestBody PlayListDTO playListDTO) {
        return playListService.createPlayList(playListDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePlayList(@PathVariable String id) {
        Long playListId = Long.parseLong(id);

        playListService.delete(playListId);
    }
}