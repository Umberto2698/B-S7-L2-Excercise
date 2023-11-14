package lezione27.controllers;

import lezione27.enteties.User;
import lezione27.exceptions.BadRequestException;
import lezione27.payloads.users.UserDTO;
import lezione27.payloads.users.UserLoginDTO;
import lezione27.payloads.users.UserSuccessLoginDTO;
import lezione27.services.AuthService;
import lezione27.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
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
            return userService.save(body);
        }
    }
}
