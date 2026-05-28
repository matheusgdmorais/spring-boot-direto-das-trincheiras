package academy.devdojo.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Anime withId(Long id) {
        return this.id == id ? this : new Anime(id, this.name);
    }

    public Anime withName(String name) {
        return this.name == name ? this : new Anime(this.id, name);
    }

}
