package academy.devdojo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserPutRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
