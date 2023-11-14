package lezione27.enteties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javafaker.Faker;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "UserBuilder")
@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String avatar;
    private String password;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime cratedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User_Device> user_devices;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static class UserBuilder {
        Faker faker = new Faker(Locale.ITALY);
        private String name = faker.name().firstName();
        private String surname = faker.name().lastName();
        private String email = name + "." + surname + "@gmail.com";
        private String username = faker.funnyName().name();
    }
}
