package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends CrudRepository<RefreshToken, Long> {

    void deleteByToken(String token);

    RefreshToken findByToken(String token);

    void deleteAllByUserEmail(String id);
}