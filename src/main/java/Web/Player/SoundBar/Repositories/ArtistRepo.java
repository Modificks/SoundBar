package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepo extends CrudRepository<Artist, Long> {

    @Query(value = "SELECT a FROM Artist a " +
            "INNER JOIN a.user u " +
            "WHERE u.id = :id")
    Artist getArtist(@Param("id") Long id);
}