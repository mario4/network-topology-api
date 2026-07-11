package devices.application;

import devices.domain.DeviceType;

public record RegisterDeviceCommand(String macAddress, DeviceType type, String uplinkMacAddress) {
}
