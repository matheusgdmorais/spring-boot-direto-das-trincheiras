package external.dependency;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Connection {
    private int localhost;
    private String username;
    private int password;
}
