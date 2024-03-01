package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Enums.UserRoles;
import Web.Player.SoundBar.Repositories.RoleRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final RoleRepo roleRepo;

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {}", user.getEmail());

        Set<UserRole> defaultRole = roleRepo.findByRoleName(UserRoles.USER);

        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickname(user.getNickname());
        user.setUserRoles(defaultRole);

        return userRepo.save(user);
    }

    @Override
    public UserRole saveRole(UserRole userRole) {
        log.info("Saving role {}", userRole.getRoleName());
        return roleRepo.save(userRole);
    }

    @Override
    public User getUser(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);

        if (user == null) {
            log.error("User was not found");
            throw new UsernameNotFoundException("User was not found");
        } else {
            log.info("User was found: {}", username);
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        user.getUserRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()))
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}