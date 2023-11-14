package lezione27.payloads.users;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String surname,
        String username,
        String email,
        String avatar) {
}
