package academy.devdojo.commons;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {


    public List<Producer> newProducerList() {
        var datetime = "2026-05-18T15:37:14.0676549";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDataTime = LocalDateTime.parse(datetime, formatter);

        var teste = Producer.builder().id(1L).name("TESTEEE").createdAt(localDataTime).build();
        var junit = Producer.builder().id(2L).name("JUUUNIT").createdAt(localDataTime).build();
        var java = Producer.builder().id(3L).name("JAVAAA").createdAt(localDataTime).build();
        return new ArrayList<>(List.of(teste, junit, java));
    }

    public Producer newProducerToSAve() {
        return Producer.builder().id(99L).name("TESTEEE").createdAt(LocalDateTime.now()).build();
    }

}
