package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.PlayListMapper;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListServiceImp implements PlayListService {

    private final PlayListRepo playListRepo;

    private final UserRepo userRepo;

    private final SongRepo songRepo;

    private final PlayListMapper playListMapper;

    @Override
    public PlayList createPlayList(PlayListDTO playListDTO) {

        User user = userRepo.findUserById(playListDTO.getUserBaseDTO().getId());

        if (playListRepo.findByNameAndUser(playListDTO.getName(), user) != null) {
            throw new ObjectIsAlreadyExistException("Play list with this name is already exists for this user");
        }

        PlayList playList = playListMapper.toEntity(playListDTO);

        playList.setName(playList.getName());
        playList.setUser(user);

        return playListRepo.save(playList);
    }

    @Override
    public void deletePlayList(PlayListDTO playListDTO) {

        User user = userRepo.findUserById(playListDTO.getUserBaseDTO().getId());

        PlayList existingPlayList = playListRepo.findByNameAndUser(playListDTO.getName(), user);

        if (existingPlayList == null) {
            throw new ObjectIsAlreadyExistException("There is no play list with such name");
        }

        existingPlayList.getPlayListsMusic().clear();

        playListRepo.delete(existingPlayList);
    }

    public void removeSongFromPlayList(Long playListId, Long songId) {

        PlayList playList = playListRepo.findPlayListById(playListId);
        Song song = songRepo.findSongById(songId);

        if (playList == null && song == null){
            throw new RuntimeException("There are no such playList and song");
        } else {
            Set<Song> songsInPlayList = playList.getPlayListsMusic();
            if (songsInPlayList.contains(song)) {
                songsInPlayList.remove(song);
                playList.setPlayListsMusic(songsInPlayList);
                playListRepo.save(playList);
            }
        }
    }

    @Override
    public void addSongToPlayList(Long playListId, Long songId) {

        PlayList playList = playListRepo.findPlayListById(playListId);
        Song song = songRepo.findSongById(songId);

        if (playList != null && song != null) {
            playList.getPlayListsMusic().add(song);
            playListRepo.save(playList);
        }
    }

    @Override
    public List<PlayList> getAllPlayLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByEmail(username);
        Long userId = user.getId();
        return playListRepo.findAllByUserId(userId);
    }
}