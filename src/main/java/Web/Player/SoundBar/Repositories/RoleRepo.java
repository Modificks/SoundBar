package Web.Player.SoundBar.Repositories;

import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Domains.Enums.UserRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<UserRole, Long> {

    UserRole findByRoleName(UserRoles roleName);
}