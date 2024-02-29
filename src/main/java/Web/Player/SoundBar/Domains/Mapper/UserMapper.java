package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.UserDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {

    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}