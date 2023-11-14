package lezione27.enteties;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_devices")
public class User_Device {
    @Id
    private UUID id;
    @Column(name = "assignment_date")
    private LocalDate assignmentDate;
    @Column(name = "withdrawal_date")
    private LocalDate withdrawalDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}
