package Web.Player.SoundBar.APIs.ArtistController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("SoundBar/artist")
public class ArtistController {

    @GetMapping("/statistic")
    public void getStatistic() {}

    @GetMapping("/salary")
    public void getSalary() {}

    @PostMapping("/upload_file")
    public void uploadFile() {}

    @DeleteMapping("/delete_song")
    public void deleteSong() {}
}