package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Enums.UserRoles;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.ArtistRepo;
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
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final RoleRepo roleRepo;

    private final UserRepo userRepo;

    private final ArtistRepo artistRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user, boolean isArtist) {
        if (userRepo.findByEmail(user.getEmail()) != null || userRepo.findByNickname(user.getNickname()) != null) {
            throw new ObjectIsAlreadyExistException("This user is already exists");
        } else {
            Set<UserRole> defaultRoles = new HashSet<>();
            defaultRoles.add(roleRepo.findByRoleName(UserRoles.USER));

            if (isArtist) {
                defaultRoles.add(roleRepo.findByRoleName(UserRoles.ARTIST));

                Artist artist = new Artist();
                artist.setNickname(user.getNickname());
                artistRepo.save(artist);
            }

            user.setEmail(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setNickname(user.getNickname());
            user.setUserRoles(defaultRoles);

            return userRepo.save(user);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User was not found");
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