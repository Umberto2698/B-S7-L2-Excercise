package lezione27.controllers;

import lezione27.enteties.User;
import lezione27.enteties.User_Device;
import lezione27.exceptions.BadRequestException;
import lezione27.payloads.users_devices.User_DeviceDTO;
import lezione27.payloads.users_devices.User_DeviceUpdateInfoDTO;
import lezione27.services.UserDeviceService;
import lezione27.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/management")
public class UserDeviceController {

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private UserService userService;


    @GetMapping("")
    public Page<User_Device> getUser_Device(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String orderBy) {
        return userDeviceService.getUser_Device(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public User_Device getInfo(@PathVariable UUID id) {
        return userDeviceService.getInfo(id);
    }

    @GetMapping("/history/{id}")
    public Page<User_Device> getUserHistory(@PathVariable UUID id, @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String orderBy) {
        User user = userService.getById(id);
        return userDeviceService.getUserHistory(user, page, size, orderBy);
    }

    @PostMapping("")
    public User_Device assignDeviceToUser(@RequestBody @Validated User_DeviceDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return userDeviceService.assignDeviceToUser(body);
        }
    }

    @PatchMapping("/{id}")
    public User_Device updateWithdrawalDate(@RequestBody @Validated User_DeviceUpdateInfoDTO body, @PathVariable UUID id, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return userDeviceService.updateWithdrawalDate(id, body);
        }
    }
}
