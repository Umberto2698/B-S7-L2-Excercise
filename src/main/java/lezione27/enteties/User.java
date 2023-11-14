package lezione27.enteties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import jakarta.persistence.*;
import lezione27.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
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
@JsonIgnoreProperties({"role", "password", "user_devices", "createdAt", "authorities", "enabled", "credentialsNonExpired", "accountNonExpired", "accountNonLocked"})
public class User implements UserDetails {
    @Id
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String avatar;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class UserBuilder {
        Faker faker = new Faker(Locale.ITALY);
        private String name = faker.name().firstName();
        private String surname = faker.name().lastName();
        private String email = name + "." + surname + "@gmail.com";
        private String username = faker.funnyName().name();
        private Role role = Role.USER;
    }
}
