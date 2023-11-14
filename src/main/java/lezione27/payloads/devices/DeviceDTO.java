package lezione27.payloads.devices;

import lezione27.enums.DeviceState;
import lezione27.enums.DeviceType;
import jakarta.validation.constraints.NotNull;

public record DeviceDTO(
        DeviceState state,
        @NotNull(message = "Insert the type of the device.")
        DeviceType type) {
}
