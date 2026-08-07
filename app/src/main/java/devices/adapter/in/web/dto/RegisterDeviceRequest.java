package devices.adapter.in.web.dto;

import devices.domain.DeviceType;

public record RegisterDeviceRequest(String macAddress, DeviceType type, String uplinkMacAddress) {

}
