package lezione27.payloads.users_devices;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record User_DeviceDTO(
        @NotNull(message = "The user id is rquired.")
        UUID userId,
        @NotNull(message = "The device id is rquired.")
        UUID deviceId,
        @NotNull(message = "The assignment date is required.")
        LocalDate assignmentDate,
        LocalDate withdrawalDate) {
}
