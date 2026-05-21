package academy.devdojo.repository;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

    private final List<User> users = new ArrayList<>(3);

    {
        var matheus = User.builder().id(1L).firstName("Matheus").lastName("Morais").email("matheusmorais.dev@gmail.com").build();
        var carol = User.builder().id(2L).firstName("Carol").lastName("Ventura").email("carolventura46@hotmail.com").build();
        var luiz = User.builder().id(3L).firstName("Luiz").lastName("Fernando").email("luizfernando46@gmail.com").build();
        users.addAll(List.of(matheus,carol,luiz));
    }

    public List<User> getUsers() {

        return users;
    }
}

