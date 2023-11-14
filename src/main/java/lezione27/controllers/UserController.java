package lezione27.controllers;

import lezione27.enteties.User;
import lezione27.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String orderBy) {
        return userService.getUsers(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUserPicture(@RequestParam("avatar") MultipartFile body, @PathVariable UUID id) throws IOException {
        return userService.uploadPicture(body, id);
    }

    @GetMapping("/me")
    public UserDetails getProfile(@AuthenticationPrincipal UserDetails currentUser) {
        return currentUser;
    }

    @PatchMapping("/me/upload")
    public User updateUserPicture(@RequestParam("avatar") MultipartFile body, @AuthenticationPrincipal User currentUser) throws IOException {
        return userService.uploadPicture(body, currentUser.getId());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProile(@AuthenticationPrincipal User currentUser) {
        userService.delete(currentUser.getId());
    }
}
