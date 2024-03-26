package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepo extends CrudRepository<Song, Long> {

    Song findSongById(Long songId);
}