package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        var naruto = Anime.builder().id(1L).name("Naruto Shippuden").build();
        var got = Anime.builder().id(2L).name("Game of Thrones").build();
        var death = Anime.builder().id(3L).name("Death Note").build();
        var dragon = Anime.builder().id(4L).name("Dragon Ball Z").build();
        animes.addAll(List.of(naruto, got, death, dragon));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
