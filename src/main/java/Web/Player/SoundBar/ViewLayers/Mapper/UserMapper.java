package Web.Player.SoundBar.ViewLayers.Mapper;

import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.LoginDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, LoginDTO> {

    @Named("toUserDto")
    User toEntity(UserRegistrationDTO userRegistrationDTO);
}