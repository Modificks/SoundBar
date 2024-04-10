package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    User findByEmail(String email);

    User findByNickname(String nickname);

    User findUserById(Long userId);

    Set<User> findAll();
}