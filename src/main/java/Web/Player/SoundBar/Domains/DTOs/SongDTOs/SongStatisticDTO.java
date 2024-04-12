package Web.Player.SoundBar.Domains.DTOs.SongDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SongStatisticDTO {
    private String title;
    private long listenCount;

    public SongStatisticDTO(long listenCount) {
        this.listenCount = listenCount;
    }
}