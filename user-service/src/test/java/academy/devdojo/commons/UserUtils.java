package academy.devdojo.commons;

import academy.devdojo.domain.User;
import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {


    public List<User> newUsersList() {
        var teste1 = User.builder().id(1L).firstName("TEST1").lastName("Morais").email("testsmorais.dev@gmail.com").build();
        var teste2 = User.builder().id(2L).firstName("TEST2").lastName("Ventura").email("testventura46@hotmail.com").build();
        var teste3 = User.builder().id(3L).firstName("TEST3").lastName("Fernando").email("testfernando46@gmail.com").build();
        return new ArrayList<>(List.of(teste1,teste2,teste3));
    }

    public User newUserToSAve() {
        return User.builder()
                .id(99L)
                .firstName("DAEMOM")
                .lastName("TARGERYAN")
                .email("daemonerhaenyra@gmail.com").build();
    }

}
