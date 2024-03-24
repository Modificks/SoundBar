package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlayListMapper extends BaseMapper<PlayList, PlayListDTO> {

    @Named("toPlayListDto")
    PlayList toEntity(PlayListDTO playListDTO);

    PlayListDTO toDto(PlayList playList);
}