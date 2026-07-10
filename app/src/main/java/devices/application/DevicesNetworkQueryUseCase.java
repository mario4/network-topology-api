package devices.application;

import devices.domain.Device;
import devices.port.out.DevicesNetworkRepository;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DevicesNetworkQueryUseCase {

    private DevicesNetworkRepository devicesNetworkRepository;

    public DevicesNetworkQueryUseCase() {
    }

    public DevicesNetworkQueryUseCase(DevicesNetworkRepository devicesNetworkRepository) {
        this.devicesNetworkRepository = devicesNetworkRepository;
    }

    public Device getRegisteredDevice(String string) {
        return devicesNetworkRepository.load().getRegisteredDevices().stream().filter(d -> d.getMacAddress().value().equals(string))
                .findFirst()
                .orElse(null);
    }

    public Set<Device> getRegisteredDevices() {
        return devicesNetworkRepository.load().getRegisteredDevices().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Device getTopology() {
        return devicesNetworkRepository.load().getTopology();
    }

}
