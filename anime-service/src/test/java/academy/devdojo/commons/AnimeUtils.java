package academy.devdojo.commons;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {


    public List<Anime> newanimesList() {
        var tyrel = Anime.builder().id(1L).name("Margie Tyrel").build();
        var baratheon = Anime.builder().id(2L).name("Stennes Baratheon").build();
        var targeryan = Anime.builder().id(3L).name("Aemond Taregeryan").build();
        var velaryon = Anime.builder().id(4L).name("Rhaenys Velaryon").build();
        return new ArrayList<>(List.of(tyrel, baratheon, targeryan, velaryon));
    }

    public Anime newanimeToSAve() {
        return Anime.builder().id(99L).name("DAEMOM TARGERYAN").build();
    }

}
