package devices.adapter.in.web;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import devices.adapter.in.web.dto.DeviceEntryResponse;
import devices.adapter.in.web.dto.DevicesNetworkTopologyResponse;
import devices.adapter.in.web.dto.RegisterDeviceRequest;
import devices.adapter.in.web.exceptions.DeviceNotFoundException;
import devices.adapter.in.web.mapper.DevicesNetworkTopologyMapper;
import devices.application.DevicesNetworkQueryUseCase;
import devices.application.RegisterDeviceCommand;
import devices.application.RegisterDeviceUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devices.domain.Device;

@RestController
@RequestMapping("/api/network/devices")
public class NetworkController {

    private final RegisterDeviceUseCase registerDeviceUseCase;

    private final DevicesNetworkQueryUseCase devicesNetworkQueryUseCase;

    public NetworkController(RegisterDeviceUseCase registerDeviceUseCase, DevicesNetworkQueryUseCase devicesNetworkQueryUseCase) {
        this.registerDeviceUseCase = registerDeviceUseCase;
        this.devicesNetworkQueryUseCase = devicesNetworkQueryUseCase;
    }

    @GetMapping("/topology")
    public DevicesNetworkTopologyResponse getNetworkTopology() {
        return DevicesNetworkTopologyMapper.map(devicesNetworkQueryUseCase.getTopology());
    }

    @PostMapping
    public void registerDevice(@Valid @RequestBody RegisterDeviceRequest request) {
        registerDeviceUseCase
                .execute(new RegisterDeviceCommand(request.macAddress(), request.type(), request.uplinkMacAddress()));
    }

    @GetMapping
    public List<DeviceEntryResponse> listRegisteredDevices() {
        return devicesNetworkQueryUseCase.getRegisteredDevices().stream()
                .map(d -> new DeviceEntryResponse(d.getMacAddress().value(), d.getType())).collect(Collectors.toList());
    }

    @GetMapping("/{macAddress}")
    public DeviceEntryResponse getRegisteredDevice(@PathVariable String macAddress) {
        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException("device not found");
        }
        return new DeviceEntryResponse(Optional.ofNullable(device.getMacAddress().value()).orElse(""), device.getType());
    }

    @GetMapping("/{macAddress}/topology")
    public DevicesNetworkTopologyResponse getRegisteredDeviceTopology(@PathVariable String macAddress) {
        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException("device not found");
        }
        return DevicesNetworkTopologyMapper.map(device);
    }
}