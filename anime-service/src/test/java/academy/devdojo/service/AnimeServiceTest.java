package academy.devdojo.service;

import academy.devdojo.domain.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> animesList;

    @BeforeEach
    void init() {
        var tyrel =  Anime.builder().id(1L).name("Margie Tyrel").build();
        var baratheon =  Anime.builder().id(2L).name("Stennes Baratheon").build();
        var targeryan =  Anime.builder().id(3L).name("Aemond Taregeryan").build();
        var velaryon =  Anime.builder().id(4L).name("Rhaenys Velaryon").build();
        animesList= new ArrayList<>(List.of(tyrel, baratheon, targeryan, velaryon));
    }

    @Test
    @DisplayName("find returns a list eith all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnime_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);
        var animes = service.findAll(null);
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animesList);
    }

    @Test
    @DisplayName("find returns list with found object when name exists ")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameIsFound() {
        var anime = animesList.getFirst();
        var expectedAnimeFound = singletonList(anime);
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimeFound);
        var animesFound = service.findAll(anime.getName());
        Assertions.assertThat(animesFound).containsAll(expectedAnimeFound);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found ")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "notfound";
        BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());
        var animes = service.findAll(name);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an anime with given id")
    @Order(4)
    void findAll_ReturnsAnimesById_WhenSucessful() {
        var expectedAnime = animesList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));
        var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());
        Assertions.assertThat(animes).isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenSucessful() {
        var expectedAnime = animesList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(99L).name("PENTOS ").build();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);
        var savedAnime = service.save(animeToSave);
        Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a anime")
    @Order(7)
    void delete_RemoveAnime_WhenSucessful() {
        var animeToDelete = animesList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusExeption when a anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusExeption_WhenAnimeIsNotFound() {
        var animeToDelete = animesList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a anime")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() {
        var animeToUpdate = animesList.getFirst();
        animeToUpdate.setName("daenerys targeryan");
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenSuccessful() {
        var animeToUpdate = animesList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

}