package Web.Player.SoundBar.Domains.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Artist implements Serializable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", length = 64)
    private String nickname;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonBackReference
    private Set<Song> song;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "salary")
    private BigDecimal salary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist artist)) return false;
        return Objects.equals(getId(), artist.getId()) && Objects.equals(getNickname(), artist.getNickname()) && Objects.equals(getSalary(), artist.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNickname(), getSalary());
    }
}