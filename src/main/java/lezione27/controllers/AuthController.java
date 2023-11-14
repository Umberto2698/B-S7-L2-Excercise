package lezione27.controllers;

import lezione27.enteties.User;
import lezione27.exceptions.BadRequestException;
import lezione27.payloads.users.*;
import lezione27.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserSuccessLoginDTO login(@RequestBody UserLoginDTO body) {

        return new UserSuccessLoginDTO(authService.authenticateUser(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return authService.save(body);
        }
    }

    @PostMapping("/role/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateRole(@PathVariable UUID id, @RequestBody @Validated RoleUpdateDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return authService.updateRole(id, body);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUserInfo(@PathVariable UUID id, @RequestBody @Validated UserUpdateInfoDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return authService.update(id, body);
        }
    }

    @PutMapping("/me")
    public UserDetails updateProfile(@AuthenticationPrincipal User currentUser, @RequestBody UserUpdateInfoDTO body) {
        return authService.update(currentUser.getId(), body);
    }
}
