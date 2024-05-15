package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import java.math.BigDecimal;
import java.util.Set;

public interface UserService extends GeneralInterface<Long> {

    User register(UserRegistrationDTO userRegistrationDTO, boolean isArtist);

    Set<User> getAllUsers();

    void addRole(Long userId, Long roleId, boolean addRole);

    BigDecimal getSalary();

    User findByEmail(String email);
}