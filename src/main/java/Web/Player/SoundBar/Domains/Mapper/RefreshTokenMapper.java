package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.RefreshTokenDTO;
import Web.Player.SoundBar.Domains.Entities.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class},
        componentModel = "spring")
public interface RefreshTokenMapper extends BaseMapper<RefreshToken, RefreshTokenDTO> {

    @Mapping(target = "user", qualifiedByName = "toUserDto")
    RefreshToken toEntity(RefreshTokenDTO refreshTokenDTO);

    RefreshTokenDTO toDto(RefreshToken refreshToken);
}