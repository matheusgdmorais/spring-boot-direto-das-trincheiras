package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserHardCodedRepository repository;
    private List<User> usersList;

    @BeforeEach
    void init() {
        usersList = userUtils.newUsersList();

    }

    @Test
    @DisplayName("find returns a list eith all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUser_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(usersList);
        var users = service.findAll(null);
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(usersList);
    }

    @Test
    @DisplayName("find returns list with found object when first name exists ")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        var user = usersList.getFirst();
        var expectedUserFound = singletonList(user);
        BDDMockito.when(repository.findByFirstName(user.getFirstName())).thenReturn(expectedUserFound);
        var usersFound = service.findAll(user.getFirstName());
        Assertions.assertThat(usersFound).containsAll(expectedUserFound);
    }

    @Test
    @DisplayName("findAll returns empty list when First name is not found ")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var firstName = "notfound";
        BDDMockito.when(repository.findByFirstName(firstName)).thenReturn(emptyList());
        var users = service.findAll(firstName);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("findById returns an user with given id")
    @Order(4)
    void findAll_ReturnsUsersById_WhenSucessful() {
        var expectedUser = usersList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        var users = service.findByIdOrThrowNotFound(expectedUser.getId());
        Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenSucessful() {
        var expectedUser = usersList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSAve();
        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        var savedUser = service.save(userToSave);
        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(7)
    void delete_RemoveUser_WhenSucessful() {
        var userToDelete = usersList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusExeption when a user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusExeption_WhenUserIsNotFound() {
        var userToDelete = usersList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = usersList.getFirst();
        userToUpdate.setFirstName("Daenerys");
        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenSuccessful() {
        var userToUpdate = usersList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}