package academy.devdojo.anime;


import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.anime","academy.devdojo.commons"})
//@Import({AnimeMapperImpl.class, AnimeService.class, AnimeHardCodedRepository.class, AnimeData.class})
class AnimeControllerTest {
    private static final String URL = "/v1/animes";

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AnimeRepository repository;
    private List<Anime> animesList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animesList = animeUtils.newanimesList();
    }


    @Test
    @DisplayName("GET v1/animes returns a list with all animes when argumente is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("/anime/get-anime-null-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/animes?name=Aemond Taregeryan returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("/anime/get-anime-Aemond-Taregeryan-null-name-200.json");
        var name = "Aemond Taregeryan";
        var teste = animesList.stream().filter(user -> user.getName().equals(name)).findFirst().orElse(null);
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(teste));
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/animes?param=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("/anime/get-anime-x-name-200.json");
        var name = "x";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/animes/1 returns an anime with given id")
    @Order(4)
    void findAll_ReturnsAnimesById_WhenSucessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animesList);
        var response = fileUtils.readResourceFile("/anime/get-anime-by-id-200.json");
        var id = 1L;
        var foundAnime= animesList.stream().filter(anime -> anime.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/animes/99 throws NotFound 404 when anime is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenSucessful() throws Exception {
        var response = fileUtils.readResourceFile("/anime/get-anime-by-id-404.json");
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("POST v1/animes creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
        var animeToSave = animeUtils.newanimeToSAve();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("PUT v1/animes updates a anime")
    @Order(7)
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        var id = animesList.getFirst().getId();
        var foundAnime= animesList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("PUT v1/animes throws NotFound when anime is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("DELETE v1/animes/1 removes a anime")
    @Order(9)
    void delete_RemoveAnime_WhenSucessful() throws Exception {
        var id = animesList.getFirst().getId();
        var foundAnime = animesList.stream().filter(anime -> anime.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }




    @Test
    @DisplayName("DELETE v1/animes/99 throws NotFound when a anime is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenSucessfulIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("/anime/delete-anime-by-id-404.json");
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are invalid")
    @Order(12)
    void save_ReturnsBadRequst_WhenFieldsAreInvalid(String fileName, List <String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));


        var mvcresult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        var resolvedException = mvcresult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }
    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are invalid")
    @Order(13)
    void update_ReturnsBadRequst_WhenFieldsAreInvalid(String fileName, List <String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));


        var mvcresult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        var resolvedException = mvcresult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);

    }
    private static Stream<Arguments> putAnimeBadRequestSource(){
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("the field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-anime-empty-fields-400.json",allRequiredErrors()),
                Arguments.of("put-request-anime-blank-fields-400.json",allRequiredErrors())
        );
    }


    private static Stream<Arguments> postAnimeBadRequestSource(){
        var allErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json",allErrors),
                Arguments.of("post-request-anime-blank-fields-400.json",allErrors)
        );
    }


    private static List<String> allRequiredErrors(){
        var nameRequiredError= "the field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }


}