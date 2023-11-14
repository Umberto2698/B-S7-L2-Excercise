package lezione27.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lezione27.enteties.User;
import lezione27.exceptions.ItemNotFoundException;
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
