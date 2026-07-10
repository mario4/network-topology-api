package devices.port.out;

import devices.domain.DevicesNetwork;

public interface DevicesNetworkRepository {

    public DevicesNetwork load();

    public void save(DevicesNetwork devicesNetwork);
}
