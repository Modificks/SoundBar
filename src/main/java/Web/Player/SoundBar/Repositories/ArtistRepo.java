package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepo extends CrudRepository<Artist, Long> {

}