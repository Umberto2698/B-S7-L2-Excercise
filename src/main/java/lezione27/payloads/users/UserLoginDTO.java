package lezione27.payloads.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UserLoginDTO(
        @NotEmpty(message = "The email is required.")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Insert a valid email.")
        String email,
        @NotEmpty(message = "The password is required.")
        String password) {
}
