package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Exceptions.ObjectIsAlreadyExistException;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Services.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListServiceImp implements PlayListService {

    private final PlayListRepo playListRepo;

    @Override
    public PlayList createPlayList(PlayList playList) {

        if (playListRepo.findByNameAndUser(playList.getName(), playList.getUser()) != null) {
            throw new ObjectIsAlreadyExistException("Play list with this name is already exists for this user");
        }

        playList.setName(playList.getName());

        return playListRepo.save(playList);
    }

    @Override
    public void deletePlayList(PlayList playList) {
        PlayList existingPlayList = playListRepo.findByNameAndUser(playList.getName(), playList.getUser());

        if (existingPlayList == null) {
            throw new ObjectIsAlreadyExistException("There is no play list with such name");
        }

        existingPlayList.getPlayListsMusic().clear();

        playListRepo.delete(existingPlayList);
    }
}