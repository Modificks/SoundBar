package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailOrNickname(String email, String nickname);

    Set<User> findAll();
}