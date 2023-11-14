package lezione27.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lezione27.enteties.User;
import lezione27.exceptions.ItemNotFoundException;
import lezione27.payloads.users.UserResponseDTO;
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

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public void delete(UUID id) throws IOException {
        User found = this.getById(id);
        cloudinary.uploader().destroy(found.getAvatar(), ObjectUtils.emptyMap());
        userRepository.delete(found);
    }

    public UserResponseDTO uploadPicture(MultipartFile file, UUID id) throws Exception {
        User found = this.getById(id);
        String publicAvatar_id = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("public_id");
        found.setAvatar(publicAvatar_id);
        userRepository.save(found);
        String url = (String) cloudinary.api().resource(publicAvatar_id, ObjectUtils.emptyMap()).get("url");
        return new UserResponseDTO(found.getId(), found.getName(), found.getSurname(), found.getUsername(), found.getEmail(), url);
    }
}
