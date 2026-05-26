package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardCodedRepository;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {

    private static final String URL = "/v1/users";

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserData userData;

    @SpyBean
    private UserHardCodedRepository repository;
    private List<User> usersList;


    @Autowired
    private FileUtils fileUtils;

    @Autowired
    UserUtils userUtils;

    @BeforeEach
    void init() {
        usersList = userUtils.newUsersList();
    }


    @Test
    @DisplayName("GET v1/users returns a list with all users when argumente is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var response = fileUtils.readResourceFile("/user/get-user-null-first-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/users?firstName=TEST3 returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var response = fileUtils.readResourceFile("/user/get-user-TEST3-null-first-name-200.json");
        var firstName = "TEST3";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when first name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var response = fileUtils.readResourceFile("/user/get-user-x-first-name-200.json");
        var firstName = "x";
        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @DisplayName("GET v1/users/1 returns an user with given id")
    @Order(4)
    void findAll_ReturnsUsersById_WhenSucessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var response = fileUtils.readResourceFile("/user/get-user-by-id-200.json");
        var id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenSucessful() throws Exception {
        var response = fileUtils.readResourceFile("/user/get-user-by-id-404.json");
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("POST v1/users creates a user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var userToSave = userUtils.newUserToSAve();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

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
    @DisplayName("PUT v1/users updates a user")
    @Order(7)
    void update_UpdatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenSuccessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");
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
    @DisplayName("DELETE v1/users/1 removes a user")
    @Order(9)
    void delete_RemoveUser_WhenSucessful() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var id = usersList.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    @DisplayName("DELETE v1/users/99 throws ResponseStatusExeption when a user is not found")
    @Order(10)
    void delete_ThrowsResponseStatusExeption_WhenSucessfulIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
        BDDMockito.when(userData.getUsers()).thenReturn(usersList);
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }
    @Test
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequst_WhenFieldsAreEmpty() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-empty-fields-400.json");


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

        var firstNameError = "the field 'firstName' is required";
        var lastNameError = "the field 'lastName' is required";
        var emailError = "the field 'email' is required";


        Assertions.assertThat(resolvedException.getMessage())
                .contains(firstNameError,lastNameError,emailError);

    }
    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are invalid")
    @Order(12)
    void save_ReturnsBadRequst_WhenFieldsAreInvalid(String fileName, List <String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));


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
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    @Order(13)
    void update_ReturnsBadRequst_WhenFieldsAreInvalid(String fileName, List <String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));


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
    private static Stream<Arguments> putUserBadRequestSource(){
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("the field 'id' cannot be null");

        var emailInvalidError = invalidEmailErrors();


        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json",allRequiredErrors()),
                Arguments.of("put-request-user-blank-fields-400.json",allRequiredErrors()),
                Arguments.of("put-request-user-invalid-email-400.json",emailInvalidError)
        );
    }




    private static Stream<Arguments> postUserBadRequestSource(){

        var allErrors = allRequiredErrors();
        var emailInvalidError = invalidEmailErrors();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json",allErrors),
                Arguments.of("post-request-user-blank-fields-400.json",allErrors),
                Arguments.of("post-request-user-invalid-email-400.json",emailInvalidError)
                );
    }

    private static List<String> invalidEmailErrors(){
        var emailInvalidError = "The e-mail is not valid";
        return List.of(emailInvalidError);
    }

    private static List<String> allRequiredErrors(){
        var firstNameRequiredError= "the field 'firstName' is required";
        var lastNameRequiredError = "the field 'lastName' is required";
        var emailRequiredError = "the field 'email' is required";
        return new ArrayList<>(List.of(firstNameRequiredError,lastNameRequiredError,emailRequiredError));
    }


}