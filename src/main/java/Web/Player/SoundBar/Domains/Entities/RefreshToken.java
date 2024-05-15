package Web.Player.SoundBar.Domains.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "refresh_tokens")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken implements Serializable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "is_used")
    private Boolean isUsed;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getToken(), that.getToken()) && Objects.equals(getIsUsed(), that.getIsUsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getIsUsed());
    }
}