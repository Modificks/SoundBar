package Web.Player.SoundBar.APIs.AdminsController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("SoundBar/admin")
public class AdminController {

    @GetMapping("/users")
    public void getUsers() {}

    @PatchMapping("update_role")
    public void changeRole() {}

    @DeleteMapping("/delete_song")
    public void deleteSong() {}

    @DeleteMapping("/delete_user")
    public void deleteUser() {}
}


/**
 * <h1>APIs</h1>
 * <p>SoundBar/admin</p> //GET, POST, PATCH, DELETE requests
 */