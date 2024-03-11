package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.Entities.PlayList;
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

        playList.setName(playList.getName());

        return playListRepo.save(playList);
    }
}