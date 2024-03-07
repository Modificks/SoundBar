package Web.Player.SoundBar.Domains.Mapper;

import Web.Player.SoundBar.Domains.DTOs.UserAuthDTO;
import Web.Player.SoundBar.Domains.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserAuthDTO> {

    @Named("toUserDto")
    User toEntity(UserAuthDTO userAuthDTO);

    UserAuthDTO toDto(User user);
}