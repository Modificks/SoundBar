package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Utils;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongDTO;
import Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs.SongStatisticDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.PlayList;
import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Domains.Entities.User;
import Web.Player.SoundBar.ViewLayers.Mapper.SongMapper;
import Web.Player.SoundBar.ViewLayers.Criterias.SongSearchCriteria;
import Web.Player.SoundBar.Repositories.ArtistRepo;
import Web.Player.SoundBar.Repositories.Criterias.SongCriteriaRepo;
import Web.Player.SoundBar.Repositories.PlayListRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Repositories.UserRepo;
import Web.Player.SoundBar.Services.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
    public List<Song> addSong(List<SongDTO> listOfSongDTO, List<MultipartFile> multipartFiles) {
        Long userId = Utils.getUserIdFromSecurityContext();

        List<String> urls = s3ServiceImpl.uploadFiles(multipartFiles);

        List<Song> songs = new ArrayList<>();

        for (int i = 0; i < listOfSongDTO.size(); i++) {

            SongDTO songDTO = listOfSongDTO.get(i);
            String url = urls.get(i);

            String title = songDTO.getTitle();

            Artist artist = artistRepo.getArtist(userId);
            Song song = songMapper.toEntity(songDTO);

            song.setTitle(title);
            song.setUrl(url);
            song.setArtist(artist);
            song.setGenre(songDTO.getGenre());
            song.setListenCount(0L);
            songs.add(song);
        }

        return (List<Song>) songRepo.saveAll(songs);
    }

    @Override
    public void delete(Long songId) {
        Song song = songRepo.findById(songId)
                .orElseThrow(EntityNotFoundException::new);

        Set<PlayList> playLists = playListRepo.findAll();

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
        s3ServiceImpl.delete(song.getUrl());

        songRepo.delete(song);
    }

    @Override
    public void removeSongFromPlayList(Long playListId, Long songId) {
        PlayList playList = playListRepo.findById(playListId)
                .orElseThrow(EntityNotFoundException::new);
        Song song = songRepo.findById(songId)
                .orElseThrow(EntityNotFoundException::new);

        Set<Song> songsInPlayList = playList.getPlayListsMusic();
        if (songsInPlayList.contains(song)) {
            songsInPlayList.remove(song);
            playList.setPlayListsMusic(songsInPlayList);
            playListRepo.save(playList);
        }
    }

    @Override
    public void addSongToPlayList(Long playListId, Long songId) {
        PlayList playList = playListRepo.findById(playListId)
                .orElseThrow(EntityNotFoundException::new);
        Song song = songRepo.findById(songId)
                .orElseThrow(EntityNotFoundException::new);

        playList.getPlayListsMusic().add(song);
        playListRepo.save(playList);
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepo.findAll();
    }

    @Override
    public Page<Song> getSongs(SongSearchCriteria songSearchCriteria, Pageable songPage) {
        return songCriteriaRepo.findAllWithFilters(songSearchCriteria, songPage);
    }

    @Override
    public Song listenToSong(Long songId) {
        Song song = songRepo.findById(songId)
                .orElseThrow(EntityNotFoundException::new);

        Long listenCount = song.getListenCount();
        listenCount++;

        song.setListenCount(listenCount);

        return songRepo.save(song);
    }

    @Override
    public SongStatisticDTO[] getSongStatistic(Long artistId) {
        Long userId = Utils.getUserIdFromSecurityContext();

        User findUser = userRepo.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Artist artist = artistRepo.findById(artistId)
                .orElseThrow(EntityNotFoundException::new);

        if (findUser.getArtist().getId().equals(artistId)) {
            return summeryStatistic(artist);
        } else {
            throw new AccessDeniedException("You can not see this info");
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