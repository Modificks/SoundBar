package Web.Player.SoundBar.APIs;

import Web.Player.SoundBar.Domains.DTOs.UserDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Services.Impl.RefreshTokenServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    private final UserMapper userMapper;

    @PostMapping("/user/save")
    public User registration(@RequestBody @Valid UserDTO userDTO) {
        return userServiceImpl.saveUser(userMapper.toEntity(userDTO), userDTO.isArtist());
    }

//    @PostMapping("/role/save")
//    public ResponseEntity<UserRole> saveRole(@RequestBody UserRole userRole) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
//        return ResponseEntity.created(uri).body(userServiceImpl.saveRole(userRole));
//    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userServiceImpl.getUsers());
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        refreshTokenServiceImpl.refreshToken(request, response);
    }

    //TODO: for refresh token endpoit will be main page of player
}
/**
 * <h1>APIs</h1>
 * <p>SoundBar</p>
 * <p>SoundBar/login, SoundBar/registration, SoundBar/player, SoundBar/player?song={}&artist={}</p>
 * <p>SoundBar/admin, SoundBar/admin/users, SoundBar/admin/updaterole, SoundBar/admin/deletesong, SoundBar/admin/deleteuser, SoundBar/artist/statisticofartists</p>
 * <p>SoundBar/artist/uploadfile, SoundBar/artist/deletesong, SoundBar/artist/salary, SoundBar/artist/statistic</p>
 */