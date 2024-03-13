package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.Entities.User;

public interface UserService {

    User saveUser(User user, boolean isArtist);

    User getUser(String email);
}