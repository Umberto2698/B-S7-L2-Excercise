package lezione27.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lezione27.enteties.User;
import lezione27.exceptions.BadRequestException;
import lezione27.exceptions.ItemNotFoundException;
import lezione27.payloads.users.UserDTO;
import lezione27.payloads.users.UserUpdateInfoDTO;
import lezione27.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Page<User> getUsers(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userRepository.findAll(pageable);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException(email));
    }

    public User save(UserDTO body) {
        userRepository.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("The email" + a.getEmail() + " is alredy used.");
        });
        User user = User.builder().name(body.name()).email(body.email()).surname(body.surname()).password(body.password()).build();
        if (body.username().isEmpty()) {
            user.setUsername(body.name());
        } else {
            user.setUsername(body.username());
        }
        user.setId(UUID.randomUUID());
        user.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());

        return userRepository.save(user);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public User update(UUID id, UserUpdateInfoDTO body) {
        User found = this.getById(id);
        if (!body.email().isEmpty()) {
            found.setEmail(body.email());
        }
        if (!body.username().isEmpty()) {
            found.setUsername(body.username());
        }
        if (!body.password().isEmpty()) {
            found.setPassword(body.password());
        }
        return userRepository.save(found);
    }

    public void delete(UUID id) {
        User found = this.getById(id);
        userRepository.delete(found);
    }

    public User uploadPicture(MultipartFile file, UUID id) throws IOException {
        User found = this.getById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(url);
        return userRepository.save(found);
    }
}
