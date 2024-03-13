package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.DTOs.PlayListDTO;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.PlayListMapper;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListServiceImp implements PlayListService {

    private final PlayListRepo playListRepo;

    private final UserRepo userRepo;

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
}