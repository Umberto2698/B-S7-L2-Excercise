package lezione27.exceptions;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(UUID id) {
        super("Nessun elemento con questo id: " + id);
    }

    public ItemNotFoundException(String email) {
        super("Utente con email " + email + " non trovato!");
    }
}
