package devices.application;

import devices.domain.Device;
import devices.domain.DevicesNetwork;
import devices.port.out.DevicesNetworkRepository;

public class RegisterDeviceUseCase {

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
