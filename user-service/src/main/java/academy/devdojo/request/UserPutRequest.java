package academy.devdojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
public class UserPutRequest {

    @NotNull(message = "the field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "the field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "the field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "the field 'email' is required")
    @Email(message = "The e-mail is not valid")
    private String email;

}
