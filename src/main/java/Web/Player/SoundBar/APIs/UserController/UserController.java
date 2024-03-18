package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Services.Impl.PlayListServiceImp;
import Web.Player.SoundBar.Services.Impl.RefreshTokenServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/SoundBar")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private final PlayListServiceImp playListServiceImp;

    private final UserMapper userMapper;

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/player/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        refreshTokenServiceImpl.refreshToken(request, response);
    }

    @GetMapping("/player")
    public void getPlayer() {}

    @PostMapping("/registration")
    public User registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        return userServiceImpl.saveUser(userMapper.toEntity(userRegistrationDTO), userRegistrationDTO.getIsArtist());
    }

    @PostMapping("/player/create_play_list")
    public PlayList createPlayList(@RequestBody PlayListDTO playListDTO){
        return playListServiceImp.createPlayList(playListDTO);
    }

    @PatchMapping("/player/delete_song_from_playList")
    public void deleteSongFromPlayList(@RequestParam("playListId") Long playListId,
                                       @RequestParam("songId") Long songId) {
        playListServiceImp.removeSongFromPlayList(playListId, songId);
    }

    @PatchMapping("/player/add_song_to_playList")
    public void addSongToPlayList(@RequestParam("playListId") Long playListId,
                                  @RequestParam("songId") Long songId) {
        playListServiceImp.addSongToPlayList(playListId, songId);
    }

    @DeleteMapping("/player/delete_play_list")
    public void deletePlayList(@RequestBody PlayListDTO playListDTO){
        playListServiceImp.deletePlayList(playListDTO);
    }
}
/**
 * <h1>APIs</h1>
 * <p>SoundBar/player?song={}&artist={}</p> //GET, POST, PATCH requests
 */