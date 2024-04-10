package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlayListMapper extends BaseMapper<PlayList, PlayListBaseDTO> {

    @Named("toPlayListDto")
    PlayList toEntity(PlayListDTO playListDTO);

    PlayListDTO toDto(PlayList playList);

    PlayListBaseDTO toBaseDto(PlayList playList);
}