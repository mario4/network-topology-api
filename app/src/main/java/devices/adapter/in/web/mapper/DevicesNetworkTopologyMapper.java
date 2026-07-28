package devices.adapter.in.web.mapper;

import devices.adapter.in.web.dto.DevicesNetworkTopologyResponse;
import devices.domain.Device;

public final class DevicesNetworkTopologyMapper {

    private DevicesNetworkTopologyMapper(){}

    public static DevicesNetworkTopologyResponse map(Device device){
        return new DevicesNetworkTopologyResponse(
                device.getMacAddress().value(),
                device.getConnectedDevices().stream().map(DevicesNetworkTopologyMapper::map).toList()
        );
    }
}
