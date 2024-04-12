package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.Domains.DTOs.SongDTOs.SongStatisticDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.Domains.Mapper.SongMapper;
import Web.Player.SoundBar.Domains.Criterias.SongPage;
import Web.Player.SoundBar.Domains.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.Repositories.ArtistRepo;
import Web.Player.SoundBar.Repositories.Criterias.SongCriteriaRepo;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final S3ServiceImpl s3ServiceImpl;

    private final SongRepo songRepo;

    private final ArtistRepo artistRepo;

    private final PlayListRepo playListRepo;

    private final UserRepo userRepo;

    private final SongCriteriaRepo songCriteriaRepo;

    private final SongMapper songMapper;

    @Override
    public List<Song> addSong(List<SongDTO> listOfSongBaseDTO, List<MultipartFile> multipartFiles) {
        List<String> urls = s3ServiceImpl.uploadFiles(multipartFiles);

        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < listOfSongBaseDTO.size(); i++) {

            SongDTO songBaseDTO = listOfSongBaseDTO.get(i);
            String url = urls.get(i);
            String title = multipartFiles.get(i).getOriginalFilename().replace(".mp3", "");

            Artist artist = artistRepo.findArtistById(songBaseDTO.getArtistId());
            Song song = songMapper.toEntity(songBaseDTO);

            song.setTitle(title);
            song.setUrl(url);
            song.setArtist(artist);
            song.setGenre(songBaseDTO.getGenre());
            song.setListenCount(0L);
            songs.add(song);
        }

        return (List<Song>) songRepo.saveAll(songs);
    }

    public void deleteSong(SongBaseDTO songBaseDTO) {
        Song song = songRepo.findSongById(songBaseDTO.getId());
        Set<PlayList> playLists = playListRepo.findAll();

        if (song == null) {
            throw new EntityNotFoundException("Can not find this song");
        } else {

            Set<Song> songsInPlayList = playLists.stream()
                    .flatMap(playList -> playList.getPlayListsMusic().stream())
                    .collect(Collectors.toSet());

            if (songsInPlayList.contains(song)) {
                songsInPlayList.remove(song);

                playLists.forEach(playList -> {
                    playList.setPlayListsMusic(songsInPlayList);
                    playListRepo.save(playList);
                });
            }
            s3ServiceImpl.deleteFileFromS3Bucket(songBaseDTO.getUrl());
            songRepo.delete(song);
        }
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepo.findAll();
    }

    @Override
    public Page<Song> getSongs(SongPage songPage, SongSearchCriteria songSearchCriteria) {
        return songCriteriaRepo.findAllWithFilters(songPage, songSearchCriteria);
    }

    public Song listenToSong(Long songId) {
        Song song = songRepo.findSongById(songId);

        Long listenCount = song.getListenCount();
        listenCount++;

        song.setListenCount(listenCount);

        return songRepo.save(song);
    }

    @Override
    public SongStatisticDTO[] getSongStatistic(Long artistId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByEmail(username);
        Artist artist = artistRepo.findArtistById(artistId);

        if (user.getArtist().getId().equals(artistId)) {
            return summeryStatistic(artist);
        } else {
            throw new RuntimeException("You can not see this info");
        }
    }

    private SongStatisticDTO[] summeryStatistic(Artist artist) {
        List<SongStatisticDTO> mostPopular = songRepo.findMostPopular(artist);
        List<SongStatisticDTO> allByListenCount = songRepo.findAllByListenCount(artist);

        Long totalAmountOfListens = songRepo.findTotalAmountOfListens(artist);

        SongStatisticDTO totalListensDTO = new SongStatisticDTO(totalAmountOfListens);
        totalListensDTO.setListenCount(totalAmountOfListens);

        List<SongStatisticDTO> combinedList = Stream.concat(allByListenCount.stream(), mostPopular.stream())
                .collect(Collectors.toList());
        combinedList.add(totalListensDTO);

        return combinedList.toArray(new SongStatisticDTO[0]);
    }
}