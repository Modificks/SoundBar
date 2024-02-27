package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;

public interface UserService {

    User savaUser(User user);

    UserRole saveRole(UserRole userRole);
}