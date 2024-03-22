package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SongMapper extends BaseMapper<Song, SongBaseDTO> {

    @Named("toSongBaseDto")
    Song toEntity(SongBaseDTO songBaseDTO);

    SongBaseDTO toDto(Song song);
}