package devices.port.out;

import devices.model.DevicesNetwork;

public interface DevicesNetworkRepository {

    public DevicesNetwork load();

    public void save(DevicesNetwork devicesNetwork);
}
