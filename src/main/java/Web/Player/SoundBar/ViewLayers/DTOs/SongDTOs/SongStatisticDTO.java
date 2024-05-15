package Web.Player.SoundBar.ViewLayers.DTOs.SongDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SongStatisticDTO {

    private String title;
    private Long listenCount;

    public SongStatisticDTO(Long listenCount) {
        this.listenCount = listenCount;
    }
}