package Web.Player.SoundBar.ViewLayers.Mapper;

import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.Entities.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SongMapper extends BaseMapper<Song, SongBaseDTO> {

    @Named("toSongBaseDto")
    Song toEntity(SongBaseDTO songBaseDTO);

    SongBaseDTO toDto(Song song);
}