package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserBaseDTO> {

    @Named("toUserDto")
    User toEntity(UserRegistrationDTO userRegistrationDTO);

    @Mapping(target = "playList", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    UserDTO toDto(User user);
}