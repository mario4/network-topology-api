package devices.port.out;

import devices.domain.DevicesNetwork;

public interface DevicesNetworkRepository {

    DevicesNetwork load();
    void save(DevicesNetwork devicesNetwork);
    void clear();
}
