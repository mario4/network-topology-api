package devices.adapter.out;

import devices.domain.DevicesNetwork;
import devices.port.out.DevicesNetworkRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDevicesNetworkRepository implements DevicesNetworkRepository {

    private DevicesNetwork devicesNetwork;

    public InMemoryDevicesNetworkRepository() {
        this.devicesNetwork = new DevicesNetwork();
    }

    public InMemoryDevicesNetworkRepository(DevicesNetwork devicesNetwork) {
        this.devicesNetwork = devicesNetwork;
    }

    @Override
    public DevicesNetwork load() {
        return devicesNetwork;
    }

    @Override
    public void save(DevicesNetwork devicesNetwork) {
    }

    @Override
    public void clear() {
        devicesNetwork = new DevicesNetwork();
    }
}
