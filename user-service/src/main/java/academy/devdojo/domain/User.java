package academy.devdojo.domain;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;


    public User withId(Long id) {
        return this.id == id ? this : new User(id, this.firstName, this.lastName, this.email);
    }

    public User withFirstName(String firstName) {
        return this.firstName == firstName ? this : new User(this.id, firstName, this.lastName, this.email);
    }

    public User withLastName(String lastName) {
        return this.lastName == lastName ? this : new User(this.id, this.firstName, lastName, this.email);
    }

    public User withEmail(String email) {
        return this.email == email ? this : new User(this.id, this.firstName, this.lastName, email);
    }
}
