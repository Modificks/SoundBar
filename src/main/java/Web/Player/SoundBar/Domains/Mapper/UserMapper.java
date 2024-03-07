package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.UserDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @Named("toUserDto")
    User toEntity(UserDTO userAuthDTO);

    UserDTO toDto(User user);
}