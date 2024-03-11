package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayListRepo extends CrudRepository<PlayList, Long> {

    PlayList findByName(String playListName);
}