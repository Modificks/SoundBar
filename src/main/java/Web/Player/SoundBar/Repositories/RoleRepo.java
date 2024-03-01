package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Enums.UserRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

//TODO: implement this functionality
@Repository
public interface RoleRepo extends CrudRepository<UserRole, Long> {

    Set<UserRole> findByRoleName(UserRoles roleName);
}