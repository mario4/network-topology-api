package devices.adapter.out;

import devices.model.DevicesNetwork;
import devices.port.out.DevicesNetworkRepository;

public class InMemoryDevicesNetworkRepository implements DevicesNetworkRepository {

    private DevicesNetwork devicesNetwork;

    @Override
    public DevicesNetwork load() {
        return devicesNetwork;
    }

    @Override
    public void save(DevicesNetwork devicesNetwork) {
        this.devicesNetwork = devicesNetwork;
    }
}
