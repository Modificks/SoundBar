package Web.Player.SoundBar.Domains.Entities;

import Web.Player.SoundBar.Domains.Enums.SongGenres;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "songs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song implements Serializable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 64)
    private String title;

    @Column(name = "genre", length = 64)
    @Enumerated(value = EnumType.STRING)
    private SongGenres genre;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    @JsonIgnore
    private Artist artist;

    @ManyToMany(mappedBy = "playListsMusic", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PlayList> playListsMusic;

    @Column(name = "listen_count")
    private Long listenCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song song)) return false;
        return Objects.equals(getId(), song.getId()) && Objects.equals(getTitle(), song.getTitle()) && getGenre() == song.getGenre() && Objects.equals(getUrl(), song.getUrl()) && Objects.equals(getListenCount(), song.getListenCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getGenre(), getUrl(), getListenCount());
    }
}