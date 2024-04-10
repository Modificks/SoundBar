package Web.Player.SoundBar.Domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangeRoleRequest {
    private Long userId;
    private Long roleId;
    private boolean addRole;
}