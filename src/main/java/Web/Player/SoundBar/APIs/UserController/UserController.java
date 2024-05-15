package Web.Player.SoundBar.APIs.UserController;

import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sound-bar")
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public Set<User> getUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/admin/change-role/{userId}/{roleId}/{hasRole}")
    public String changeRole(@PathVariable String userId,
                             @PathVariable String roleId,
                             @PathVariable String hasRole) {

        Long uId = Long.parseLong(userId);
        Long rId = Long.parseLong(roleId);
        boolean hR = Boolean.parseBoolean(hasRole);

        userService.addRole(uId, rId, hR);

        return "Role has been changed";
    }

    @DeleteMapping(value = {
            "/admin/delete/{id}",
            "/player/delete/{id}"
    })
    public void deleteUser(@PathVariable String id) {
        Long userId = Long.parseLong(id);

        userService.delete(userId);
    }
}