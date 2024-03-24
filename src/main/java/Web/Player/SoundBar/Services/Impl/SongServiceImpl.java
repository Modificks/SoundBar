package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Domains.DTOs.SongBaseDTO;
import Web.Player.SoundBar.Domains.Entities.Artist;
import Web.Player.SoundBar.Domains.Entities.Song;
import Web.Player.SoundBar.Domains.Mapper.SongMapper;
import Web.Player.SoundBar.Repositories.ArtistRepo;
import Web.Player.SoundBar.Repositories.SongRepo;
import Web.Player.SoundBar.Services.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final S3ServiceImpl s3ServiceImpl;

    private final SongRepo songRepo;

    private final ArtistRepo artistRepo;

    private final SongMapper songMapper;

    @Override
    public List<Song> addSong(List<SongBaseDTO> listOfSongBaseDTO, List<MultipartFile> multipartFiles) {
        List<String> urls = s3ServiceImpl.uploadFiles(multipartFiles);

        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < listOfSongBaseDTO.size(); i++) {
            SongBaseDTO songBaseDTO = listOfSongBaseDTO.get(i);
            String url = urls.get(i);
            String title = multipartFiles.get(i).getOriginalFilename().replace(".mp3", "");

            Artist artist = artistRepo.findArtistById(songBaseDTO.getArtistId());
            Song song = songMapper.toEntity(songBaseDTO);

            song.setTitle(title);
            song.setUrl(url);
            song.setArtist(artist);
            songs.add(song);
        }

        return (List<Song>) songRepo.saveAll(songs);
    }
}