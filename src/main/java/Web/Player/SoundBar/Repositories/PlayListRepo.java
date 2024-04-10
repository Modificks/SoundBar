package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface PlayListRepo extends CrudRepository<PlayList, Long> {

    PlayList findByNameAndUser(String playListName, User user);

    PlayList findPlayListById(Long playListId);

    Set<PlayList> findAll();

    Set<PlayList> findByUserId(Long userId);

    List<PlayList> findAllByUserId(Long userId);
}