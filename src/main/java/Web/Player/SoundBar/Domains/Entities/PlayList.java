package Web.Player.SoundBar.Domains.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "play_lists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "play_lists_music",
            joinColumns = {@JoinColumn(name = "play_list_id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id")})
    @JsonIgnore
    private Set<Song> playListsMusic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayList playList)) return false;
        return Objects.equals(getId(), playList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}