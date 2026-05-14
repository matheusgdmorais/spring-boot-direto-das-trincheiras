package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    private static final List<Anime> ANIME = new ArrayList<>();

    static {
        var naruto =  Anime.builder().id(1L).name("Naruto Shippuden").build();
        var got =  Anime.builder().id(2L).name("Game of Thrones").build();
        var death =  Anime.builder().id(3L).name("Death Note").build();
        var dragon =  Anime.builder().id(4L).name("Dragon Ball Z").build();
        ANIME.addAll(List.of(naruto,got,death,dragon));
    }

    public List<Anime> findAll() {
        return ANIME;
    }

    public Optional<Anime> findById(Long id){
        return ANIME.stream()
                .filter(Anime -> Anime.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name){
        return ANIME.stream()
                .filter(Anime -> Anime.getName().equalsIgnoreCase(name))
                .toList();
    }
    public Anime save (Anime Anime){
        ANIME.add(Anime);
        return Anime;
    }

    public void delete(Anime Anime){
        ANIME.remove(Anime);
    }

    public void update(Anime Anime){
        delete(Anime);
        save(Anime);
    }
}
















