package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Utils;
import Web.Player.SoundBar.ViewLayers.DTOs.UserDTOs.UserRegistrationDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Entities.UserRole;
import Web.Player.SoundBar.ViewLayers.Mapper.UserMapper;
import Web.Player.SoundBar.Domains.Enums.UserRoles;
import Web.Player.SoundBar.Exceptions.RoleException;
import Web.Player.SoundBar.Repositories.ArtistRepo;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.RoleRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final BigDecimal COEFFICIENT = BigDecimal.valueOf(0.2);

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;
    private final PlayListRepo playListRepo;
    private final SongRepo songRepo;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public User register(UserRegistrationDTO userRegistrationDTO, boolean isArtist) {

        if (userRepo.existsByEmailOrNickname(userRegistrationDTO.getEmail(), userRegistrationDTO.getNickname())) {
            throw new ObjectIsAlreadyExistException("User already exists");
        }

        User user = userMapper.toEntity(userRegistrationDTO);

        Set<UserRole> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepo.findByRoleName(UserRoles.USER));

        user.setEmail(userRegistrationDTO.getEmail());
        user.setNickname(userRegistrationDTO.getNickname());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setUserRoles(defaultRoles);

        userRepo.save(user);

        if (isArtist) {
            defaultRoles.add(roleRepo.findByRoleName(UserRoles.ARTIST));

            saveArtist(user);
        }

        return user;
    }

    private void saveArtist(User user) {
        Artist artist = new Artist();

        artist.setNickname(user.getNickname());
        artist.setUser(user);
        artist.setSalary(BigDecimal.valueOf(0));

        artistRepo.save(artist);
    }

    @Override
    public Set<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void addRole(Long userId, Long roleId, boolean addRole) {
        User user = userRepo.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        UserRole roleEntity = roleRepo.findById(roleId)
                .orElseThrow(EntityExistsException::new);

        Set<UserRole> userRoles = user.getUserRoles();

        if (addRole) {
            if (userRoles.contains(roleEntity)) {
                throw new RoleException("User already has this role");
            }

            if (roleEntity.getRoleName() == UserRoles.ARTIST) {
                saveArtist(user);
                userRoles.add(roleEntity);
            }

        } else {
            if (!userRoles.contains(roleEntity)) {
                throw new RoleException("User does not have this role");
            }

            if (roleEntity.getRoleName() == UserRoles.ARTIST) {
                artistRepo.delete(user.getArtist());
                userRoles.remove(roleEntity);
            }
        }

        user.setUserRoles(userRoles);
        userRepo.save(user);
    }

    @Override
    public void delete(Long useId) {
        User user = userRepo.findById(useId)
                .orElseThrow(EntityNotFoundException::new);

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
        return userRepo.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }

    //Salary is count by formula: (total amount of listens) / 2 - 20%
    @Override
    public BigDecimal getSalary() {
        Long userId = Utils.getUserIdFromSecurityContext();

        Artist artist = artistRepo.getArtist(userId);

        Long totalAmountOfSongsListening = songRepo.findTotalAmountOfListens(artist);

        if (totalAmountOfSongsListening == null) {
            return BigDecimal.valueOf(0);
        }

        BigDecimal totalAmountInDollars = BigDecimal.valueOf(totalAmountOfSongsListening).divide(BigDecimal.valueOf(2));
        BigDecimal nettSalary = totalAmountInDollars.subtract(COEFFICIENT);

        setArtistSalary(artist, nettSalary);

        return nettSalary;
    }

    private void setArtistSalary(Artist artist, BigDecimal salary) {
        artist.setSalary(salary);
    }

    //Uncomment this method and comment method above to use query for counting
//    @Override
//    public BigDecimal getSalary() {
//        Long userId = utils.returnUserId();
//
//        Artist artist = artistRepo.getArtist(userId);
//
//        Long nettSalary = songRepo.findTotalAmountOfListens(artist););
//
//        artist.setSalary(nettSalary);
//
//        return nettSalary;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(EntityNotFoundException::new);

        Set<SimpleGrantedAuthority> authorities = user.getUserRoles()
                .stream()
                .map(v -> new SimpleGrantedAuthority(v.getRoleName().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}