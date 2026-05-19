package academy.devdojo.controller;


import academy.devdojo.domain.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages= "academy.devdojo")
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ProducerData producerData;
    private List<Producer> producersList;

    @SpyBean
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        var datetime= "2026-05-18T15:37:14.0676549";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDataTime = LocalDateTime.parse(datetime,formatter);

        var teste = Producer.builder().id(1L).name("TESTEEE").createdAt(localDataTime).build();
        var junit = Producer.builder().id(2L).name("JUUUNIT").createdAt(localDataTime).build();
        var java = Producer.builder().id(3L).name("JAVAAA").createdAt(localDataTime).build();
        producersList= new ArrayList<>(List.of(teste, junit, java));
    }



    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argumente is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("/producer/get-producer-null-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers?name=TESTEEE returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("/producer/get-producer-TESTEEE-null-name-200.json");
        var name = "TESTEEE";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers?param=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("/producer/get-producer-x-name-200.json");
        var name ="x";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers/1 returns an producer with given id")
    @Order(4)
    void findAll_ReturnsProducersById_WhenSucessful()  throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var response = readResourceFile("/producer/get-producer-by-id-200.json");
        var id =1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers/99 throws ResponseStatusException 404 when producer is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenSucessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var id =99L;
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }


    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful()throws Exception {
        var request = readResourceFile("producer/post-request-producer-200.json");
        var response = readResourceFile("producer/post-response-producer-201.json");
        var producerToSave= Producer.builder().id(99L).name("TESTEEE").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("PUT v1/producers updates a producer")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() throws Exception{
        var request = readResourceFile("producer/put-request-producer-200.json");
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("PUT v1/producers throws ResponseStatusException when producer is not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var request = readResourceFile("producer/put-request-producer-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }


    @Test
    @DisplayName("DELETE v1/producers/1 removes a producer")
    @Order(9)
    void delete_RemoveProducer_WhenSucessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var id = producersList.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("DELETE v1/producers/99 throws ResponseStatusExeption when a producer is not found")
    @Order(10)
    void delete_ThrowsResponseStatusExeption_WhenSucessfulIsNotFound() throws Exception{
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var id =99L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));

    }


    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}