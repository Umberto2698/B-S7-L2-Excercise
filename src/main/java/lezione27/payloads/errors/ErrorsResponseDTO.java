package lezione27.payloads.errors;

import java.util.Date;

public record ErrorsResponseDTO(String message, Date timestamp) {
}
