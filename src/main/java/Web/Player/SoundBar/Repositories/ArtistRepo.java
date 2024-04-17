package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepo extends CrudRepository<Artist, Long> {

    Artist findArtistById(Long artistId);

    @Query(value = "SELECT a.id FROM Artist a " +
            "INNER JOIN a.user u " +
            "WHERE u.email = :email")
    Long getArtist(@Param("email") String email);
}