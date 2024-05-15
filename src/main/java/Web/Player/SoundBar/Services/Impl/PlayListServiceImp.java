package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Utils;
import Web.Player.SoundBar.ViewLayers.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.ViewLayers.Mapper.PlayListMapper;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListServiceImp implements PlayListService {

    private final PlayListRepo playListRepo;
    private final UserRepo userRepo;

    private final PlayListMapper playListMapper;

    @Override
    public PlayList createPlayList(PlayListDTO playListDTO) {
        Long userId = Utils.getUserIdFromSecurityContext();

        User user = userRepo.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (playListRepo.findByNameAndUser(playListDTO.getName(), user) != null) {
            throw new ObjectIsAlreadyExistException("Play list with this name is already exists for this user");
        }

        PlayList playList = playListMapper.toEntity(playListDTO);

        playList.setName(playList.getName());
        playList.setUser(user);

        return playListRepo.save(playList);
    }

    @Override
    public void delete(Long playListId) {
        Long userId = Utils.getUserIdFromSecurityContext();

        User findUser = userRepo.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        PlayList playList = playListRepo.findById(playListId)
                .orElseThrow(EntityNotFoundException::new);

        PlayList existingPlayList = playListRepo.findByNameAndUser(playList.getName(), findUser);

        if (existingPlayList == null) {
            throw new EntityNotFoundException("There is no play list with such name");
        }

        existingPlayList.getPlayListsMusic().clear();

        playListRepo.delete(existingPlayList);
    }

    @Override
    public List<PlayList> getAllPlayLists() {
        Long userId = Utils.getUserIdFromSecurityContext();

        return playListRepo.findAllByUserId(userId);
    }
}