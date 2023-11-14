package lezione27.services;

import lezione27.enteties.User;
import lezione27.exceptions.BadRequestException;
import lezione27.exceptions.UnauthorizedException;
import lezione27.payloads.users.RoleUpdateDTO;
import lezione27.payloads.users.UserDTO;
import lezione27.payloads.users.UserLoginDTO;
import lezione27.payloads.users.UserUpdateInfoDTO;
import lezione27.repositories.UserRepository;
import lezione27.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = userService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Email or password invalid.");
        }
    }

    public User save(UserDTO body) {
        userRepository.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("The email" + a.getEmail() + " is alredy used.");
        });
        User user = User.builder().name(body.name()).email(body.email()).surname(body.surname()).password(bcrypt.encode(body.password())).build();
        if (body.username().isEmpty()) {
            user.setUsername(body.name());
        } else {
            user.setUsername(body.username());
        }
        user.setId(UUID.randomUUID());
        user.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());

        return userRepository.save(user);
    }

    public User update(UUID id, UserUpdateInfoDTO body) {
        User found = userService.getById(id);
        if (!body.email().isEmpty()) {
            found.setEmail(body.email());
        }
        if (!body.username().isEmpty()) {
            found.setUsername(body.username());
        }
        if (!body.password().isEmpty()) {
            found.setPassword(bcrypt.encode(body.password()));
        }
        return userRepository.save(found);
    }

    public User updateRole(UUID id, RoleUpdateDTO body) {
        User found = userService.getById(id);
        found.setRole(body.role());
        return userRepository.save(found);
    }
}
