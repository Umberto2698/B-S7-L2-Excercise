package lezione27.services;

import lezione27.enteties.Device;
import lezione27.enteties.User;
import lezione27.enteties.User_Device;
import lezione27.enums.DeviceState;
import lezione27.exceptions.BadRequestException;
import lezione27.exceptions.ItemNotAvailableException;
import lezione27.exceptions.ItemNotFoundException;
import lezione27.payloads.users_devices.User_DeviceDTO;
import lezione27.payloads.users_devices.User_DeviceUpdateInfoDTO;
import lezione27.repositories.DeviceRepository;
import lezione27.repositories.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDeviceService {
    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    public Page<User_Device> getUser_Device(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userDeviceRepository.findAll(pageable);
    }

    public User_Device getInfo(UUID id) {
        return userDeviceRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Page<User_Device> getUserHistory(User user, int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userDeviceRepository.findByUser(user, pageable);
    }

    public User_Device assignDeviceToUser(User_DeviceDTO body) {
        Device device = deviceService.getById(body.deviceId());
        User user = userService.getById(body.userId());
        if (device.getState() != DeviceState.AVAILABLE) {
            throw new ItemNotAvailableException("The Device is not available.");
        } else {
            device.setState(DeviceState.ASSIGNED);
            User_Device assign = User_Device.builder().user(user).device(device).assignmentDate(body.assignmentDate()).build();
            if (body.withdrawalDate() != null) {
                assign.setWithdrawalDate(body.withdrawalDate());
            }
            assign.setId(UUID.randomUUID());
            deviceRepository.save(device);
            return userDeviceRepository.save(assign);
        }
    }

    public User_Device updateWithdrawalDate(UUID id, User_DeviceUpdateInfoDTO body) {
        User_Device found = this.getInfo(id);
        if (body.withdrawalDate().isAfter(found.getWithdrawalDate())) {
            found.setWithdrawalDate(body.withdrawalDate());
        } else {
            throw new BadRequestException("The withdrawal date must be later than the assignment date.");
        }
        return userDeviceRepository.save(found);
    }
}
