package Web.Player.SoundBar.APIs.AdminsController;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserFindDTO;
import Web.Player.SoundBar.Domains.UserChangeRoleRequest;
import Web.Player.SoundBar.Services.Impl.SongServiceImpl;
import Web.Player.SoundBar.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/SoundBar/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SongServiceImpl songServiceImpl;

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/users")
    public Set<UserFindDTO> getUsers() {
        return userServiceImpl.getAllUsers();
    }

    @PatchMapping("/update-role")
    public void changeRole(@RequestBody UserChangeRoleRequest userChangeRoleRequest) {
        userServiceImpl.changeUserRole(userChangeRoleRequest.getUserId(), userChangeRoleRequest.getRoleId(), userChangeRoleRequest.isAddRole());
    }

    @DeleteMapping("/delete-song")
    public void deleteSong(@RequestBody SongBaseDTO songBaseDTO) {
        songServiceImpl.deleteSong(songBaseDTO);
    }

    @DeleteMapping("/delete-user")
    public void deleteUser(@RequestBody UserBaseDTO userBaseDTO) {
        userServiceImpl.deleteUser(userBaseDTO);
    }
}

//TODO: add statistic about user(registration date, playList, etc)