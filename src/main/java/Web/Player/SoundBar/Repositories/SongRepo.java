package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongStatisticDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepo extends CrudRepository<Song, Long> {

    Song findSongById(Long songId);

    List<Song> findAll();

    @Query(value = "SELECT SUM(s.listenCount) AS total_listen_count FROM Song s " +
            "WHERE s.artist = :artist")
    Long findTotalAmountOfListens(@Param("artist") Artist artist);

    @Query(value = "SELECT new Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongStatisticDTO(s.title, s.listenCount) " +
            "FROM Song s " +
            "WHERE s.artist = :artist " +
            "ORDER BY s.listenCount DESC")
    List<SongStatisticDTO> findAllByListenCount(@Param("artist") Artist artist);

    @Query(value = "SELECT new Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongStatisticDTO(s.title, s.listenCount) " +
            "FROM Song s " +
            "WHERE s.listenCount = (SELECT MAX(s.listenCount) FROM Song s WHERE s.artist = :artist) " +
            "AND s.artist = :artist")
    List<SongStatisticDTO> findMostPopular(@Param("artist") Artist artist);

}