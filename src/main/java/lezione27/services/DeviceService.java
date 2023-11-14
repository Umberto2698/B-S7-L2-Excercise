package lezione27.services;

import lezione27.enteties.Device;
import lezione27.enums.DeviceState;
import lezione27.exceptions.ItemNotFoundException;
import lezione27.payloads.devices.DeviceDTO;
import lezione27.payloads.devices.DeviceUpdateInfoDTO;
import lezione27.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Page<Device> getDevices(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return deviceRepository.findAll(pageable);
    }

    public Device save(DeviceDTO body) {
        Device device;
        if (body.state() == null) {
            device = Device.builder().state(DeviceState.AVAILABLE).type(body.type()).build();
        } else {
            device = Device.builder().state(body.state()).type(body.type()).build();
        }
        device.setId(UUID.randomUUID());
        return deviceRepository.save(device);
    }

    public Device getById(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Device update(UUID id, DeviceUpdateInfoDTO body) {
        Device found = this.getById(id);
        found.setState(DeviceState.valueOf(body.state()));
        return deviceRepository.save(found);
    }

    public void delete(UUID id) {
        Device found = this.getById(id);
        deviceRepository.delete(found);
    }
}
