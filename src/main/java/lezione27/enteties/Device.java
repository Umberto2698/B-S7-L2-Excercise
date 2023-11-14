package lezione27.enteties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lezione27.enums.DeviceState;
import lezione27.enums.DeviceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "devices")
public class Device {
    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private DeviceState state;
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime cratedAt;

    @OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User_Device> user_devices;

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", state=" + state +
                ", type=" + type +
                '}';
    }
    //    @Enumerated(EnumType.STRING)
    //    private DeviceState state = DeviceState.AVAILABLE;
    //    @Enumerated(EnumType.STRING)
    //    private DeviceType type;
}
