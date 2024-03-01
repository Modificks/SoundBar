package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.UserDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @Mapping(target = "id", source = "userDTO.id")
    @Mapping(target = "email", source = "userDTO.email")
    @Mapping(target = "nickname", source = "userDTO.nickname")
    @Mapping(target = "password", source = "userDTO.password")
    @Mapping(target = "userRoles", source = "userDTO.userRoles")
    @Mapping(target = "playList", source = "userDTO.playList")
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}