package academy.devdojo.repository;

import academy.devdojo.commons.ProducerUtils;
import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;
    private List<Producer> producersList;

    @InjectMocks
    private ProducerUtils producerUtils;


    @BeforeEach
    void init() {
        producersList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("find All returns a list with all producers")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producersList);
    }

    @Test
    @DisplayName("findById returns an producer with given id")
    @Order(2)
    void findAll_ReturnsProducersById_WhenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var expectedProducer = producersList.getFirst();
        var producers = repository.findById(expectedProducer.getId());
        Assertions.assertThat(producers).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null ")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists ")
    @Order(4)
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
        var expectedProducer = producersList.getFirst();
        var producers = repository.findByName(expectedProducer.getName());
        Assertions.assertThat(producers).hasSize(1).contains(expectedProducer);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        var producerToSave = producerUtils.newProducerToSAve();
        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
        var producerSavedOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemoveProducer_WhenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        var producerToDelete = producersList.getFirst();
        repository.delete(producerToDelete);

        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update updates a producer")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);

        var producerToUpdate = this.producersList.getFirst();
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.producersList).contains(producerToUpdate);

        var producerUpdateOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdateOptional).isPresent();
        Assertions.assertThat(producerUpdateOptional.get().getName()).isEqualTo(producerToUpdate.getName());

    }
}