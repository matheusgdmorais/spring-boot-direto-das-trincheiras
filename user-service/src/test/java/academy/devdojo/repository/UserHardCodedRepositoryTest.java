package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {

    @InjectMocks
    private UserHardCodedRepository repository;

    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserData userData;
    private List<User> userList;


    @BeforeEach
    void init() {

        userList = userUtils.newUsersList();
    }

    @Test
    @DisplayName("find All returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSucessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var users = repository.findAll();
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns an user with given id")
    @Order(2)
    void findAll_ReturnsUsersById_WhenSucessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var expectedUser = userList.getFirst();
        var users = repository.findById(expectedUser.getId());
        Assertions.assertThat(users).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null ")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var users = repository.findByFirstName(null);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists ")
    @Order(4)
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var expectedUser = userList.getFirst();
        var users = repository.findByFirstName(expectedUser.getFirstName());
        Assertions.assertThat(users).hasSize(1).contains(expectedUser);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = userUtils.newUserToSAve();
        var user = repository.save(userToSave);

        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
        var userSavedOptional = repository.findById(userToSave.getId());
        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes an user")
    @Order(6)
    void delete_RemoveUser_WhenSucessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToDelete = userList.getFirst();
        repository.delete(userToDelete);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(7)
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToUpdate = this.userList.getFirst();
        userToUpdate.setFirstName("Khal");

        repository.update(userToUpdate);

        Assertions.assertThat(this.userList).contains(userToUpdate);

        var userUpdateOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdateOptional).isPresent();
        Assertions.assertThat(userUpdateOptional.get().getFirstName()).isEqualTo(userToUpdate.getFirstName());

    }
}