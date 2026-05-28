package academy.devdojo.producer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProducerPostRequest {

    @NotBlank(message = "the field 'name' is required")
    private String name;

}
