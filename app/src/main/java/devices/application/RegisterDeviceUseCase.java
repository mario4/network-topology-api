package devices.application;

import devices.domain.Device;
import devices.domain.DevicesNetwork;
import devices.port.out.DevicesNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterDeviceUseCase {

    @Autowired
    private DevicesNetworkRepository networkRepository;

    public RegisterDeviceUseCase(DevicesNetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void execute(RegisterDeviceCommand command) {

        DevicesNetwork devicesNetwork = networkRepository.load();

        Device device = new Device(command.macAddress(), command.type(), command.uplinkMacAddress());

        devicesNetwork.registerDevice(device);

        networkRepository.save(devicesNetwork);
    }
}
