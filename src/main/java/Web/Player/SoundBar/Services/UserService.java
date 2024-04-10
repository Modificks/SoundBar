package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserFindDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import java.util.Set;

public interface UserService {

    User saveUser(User user, boolean isArtist);

    User getUser(String email);

    Set<UserFindDTO> getAllUsers();

    void changeUserRole(Long userId, Long roleId, boolean addRole);

    void deleteUser(UserBaseDTO userBaseDTO);

}