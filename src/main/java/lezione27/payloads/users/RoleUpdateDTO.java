package lezione27.payloads.users;

import jakarta.validation.constraints.NotNull;
import lezione27.enums.Role;

public record RoleUpdateDTO(
        @NotNull(message = "The role is required.")
        Role role) {
}
