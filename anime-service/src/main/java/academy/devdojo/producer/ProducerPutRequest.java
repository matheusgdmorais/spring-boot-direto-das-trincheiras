package academy.devdojo.producer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProducerPutRequest {

    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "the field 'name' is required")
    private String name;

}
