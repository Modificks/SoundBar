package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import java.util.List;

public interface UserService {

    User saveUser(User user);

//    UserRole saveRole(UserRole userRole);

    User getUser(String email);

    List<User> getUsers();
}