package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTOs.PlayListBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.UserDTOs.UserFindDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.Domains.Mapper.PlayListMapper;
import Web.Player.SoundBar.Domains.Mapper.UserMapper;
import Web.Player.SoundBar.Enums.UserRoles;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.ArtistRepo;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.RoleRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final BigDecimal COEFFICIENT = BigDecimal.valueOf(0.2);

    private final RoleRepo roleRepo;

    private final UserRepo userRepo;

    private final ArtistRepo artistRepo;

    private final PlayListRepo playListRepo;

    private final SongRepo songRepo;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final PlayListMapper playListMapper;

    @Override
    public User saveUser(User user, boolean isArtist) {
        if (userRepo.findByEmail(user.getEmail()) != null || userRepo.findByNickname(user.getNickname()) != null) {
            throw new ObjectIsAlreadyExistException("This user already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEmail(user.getEmail());
            user.setNickname(user.getNickname());

            Set<UserRole> defaultRoles = new HashSet<>();
            defaultRoles.add(roleRepo.findByRoleName(UserRoles.USER));
            user.setUserRoles(defaultRoles);

            User savedUser = userRepo.save(user);

            if (isArtist) {
                defaultRoles.add(roleRepo.findByRoleName(UserRoles.ARTIST));

                Artist artist = new Artist();

                artist.setNickname(user.getNickname());
                artist.setUser(savedUser);
                artist.setSalary(BigDecimal.valueOf(0));
                artistRepo.save(artist);
            }

            return savedUser;
        }
    }

    @Override
    public User getUser(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Set<UserFindDTO> getAllUsers() {
        Set<User> users = userRepo.findAll();
        Set<UserFindDTO> userDTOS = new HashSet<>();

        users.forEach(user -> {
            Set<PlayList> playLists = playListRepo.findByUserId(user.getId());
            Set<PlayListBaseDTO> playListDTOs = playLists.stream()
                    .map(playListMapper::toBaseDto)
                    .collect(Collectors.toSet());

            UserFindDTO userFindDTO = userMapper.toFindDto(user);
            userFindDTO.setPlayList(playListDTOs);

            userDTOS.add(userFindDTO);
        });

        return userDTOS;
    }

    public void changeUserRole(Long userId, Long roleId, boolean addRole) {
        User user = userRepo.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserRole roleEntity = roleRepo.findRoleById(roleId);
        if (roleEntity == null) {
            throw new RuntimeException("Role not found");
        }

        Set<UserRole> userRoles = user.getUserRoles();

        if (addRole) {
            if (userRoles.contains(roleEntity)) {
                throw new RuntimeException("User already has this role");
            }
            userRoles.add(roleEntity);
        } else {
            if (!userRoles.contains(roleEntity)) {
                throw new RuntimeException("User does not have this role");
            }
            userRoles.remove(roleEntity);
        }

        user.setUserRoles(userRoles);
        userRepo.save(user);
    }

    @Override
    public void deleteUser(UserBaseDTO userBaseDTO) {
        User user = userMapper.toEntity(userBaseDTO);
        Set<PlayList> playLists = playListRepo.findByUserId(user.getId());

        playLists.forEach(playList -> {
            playList.getPlayListsMusic().clear();
            playListRepo.save(playList);
        });

        if (user.getUserRoles() != null) {
            user.getUserRoles().clear();
            userRepo.save(user);
        }

        userRepo.deleteById(user.getId());
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    //Salary is count by formula: (total amount of listens) / 2 - 20%
    @Override
    public BigDecimal getSalary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getEmail();

        Long artistId = artistRepo.getArtist(username);
        Artist artist = artistRepo.findArtistById(artistId);
        Long listening = songRepo.findTotalAmountOfListens(artist);

        if (listening == null) {
            return BigDecimal.valueOf(0);
        }

        BigDecimal listeningAsBigDecimal = BigDecimal.valueOf(listening);
        BigDecimal totalAmountInDollars = listeningAsBigDecimal.divide(BigDecimal.valueOf(2));
        BigDecimal salary = totalAmountInDollars.subtract(COEFFICIENT);

        artist.setSalary(salary);

        return salary;
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