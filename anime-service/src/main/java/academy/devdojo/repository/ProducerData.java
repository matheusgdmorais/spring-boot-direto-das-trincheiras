package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {

    private  final List<Producer> producers = new ArrayList<>();

     {
        var kishimoto = Producer.builder().id(1L).name("Massaki Kishimoto").createdAt(LocalDateTime.now()).build();
        var george = Producer.builder().id(2L).name("George R.R. Martin").createdAt(LocalDateTime.now()).build();
        var tsugumi = Producer.builder().id(3L).name("Tsugumi Ohba").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(kishimoto, george, tsugumi));
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
