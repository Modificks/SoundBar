package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayListRepo extends CrudRepository<PlayList, Long> {

    PlayList findByNameAndUser(String playListName, User user);

    PlayList findPlayListById(Long playListId);

    List<PlayList> findAll();

    List<PlayList> findAllByUserId(Long userId);
}