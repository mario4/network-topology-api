package devices.adapter.in.web.dto;

import devices.domain.DeviceType;

public record DeviceEntryResponse(String macAddress, DeviceType type) {
}
