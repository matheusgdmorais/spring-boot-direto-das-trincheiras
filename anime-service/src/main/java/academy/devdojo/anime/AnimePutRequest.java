package academy.devdojo.anime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AnimePutRequest {

    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "the field 'name' is required")
    private String name;
}
