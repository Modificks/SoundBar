package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Domains.DTOs.UserDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Services.Impl.RefreshTokenServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private final UserMapper userMapper;

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/player")
    public void getPlayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        refreshTokenServiceImpl.refreshToken(request, response);
    }

    @PostMapping("/registration")
    public User registerUser(@Valid @RequestBody UserDTO userDTO) {
        return userServiceImpl.saveUser(userMapper.toEntity(userDTO), userDTO.getIsArtist());
    }
}
/**
 * <h1>APIs</h1>
 * <p>SoundBar/player?song={}&artist={}</p> //GET, POST, PATCH requests
 */