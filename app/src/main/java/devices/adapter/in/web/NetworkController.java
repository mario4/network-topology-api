package devices.adapter.in.web;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import devices.adapter.in.web.dto.DeviceEntryResponse;
import devices.adapter.in.web.dto.DevicesNetworkTopologyResponse;
import devices.adapter.in.web.dto.ErrorResponse;
import devices.adapter.in.web.dto.RegisterDeviceRequest;
import devices.adapter.in.web.exceptions.DeviceNotFoundException;
import devices.adapter.in.web.mapper.DevicesNetworkTopologyMapper;
import devices.adapter.out.InMemoryDevicesNetworkRepository;
import devices.application.DevicesNetworkQueryUseCase;
import devices.application.RegisterDeviceCommand;
import devices.application.RegisterDeviceUseCase;
import devices.port.out.DevicesNetworkRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devices.domain.Device;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    private DevicesNetworkRepository devicesNetworkRepository = new InMemoryDevicesNetworkRepository();

    private RegisterDeviceUseCase registerDeviceUseCase = new RegisterDeviceUseCase(devicesNetworkRepository);;

    private DevicesNetworkQueryUseCase devicesNetworkQueryUseCase = new DevicesNetworkQueryUseCase(devicesNetworkRepository);

    @GetMapping("/topology")
    public DevicesNetworkTopologyResponse getNetworkTopology() {
        return DevicesNetworkTopologyMapper.map(devicesNetworkQueryUseCase.getTopology());
    }

    @PostMapping("/devices/register")
    public void registerDevice(@RequestBody RegisterDeviceRequest request) {

        validateRegisterDeviceRequest(request);

        registerDeviceUseCase
                .execute(new RegisterDeviceCommand(request.getMacAddress(), request.getType(), request.getUplinkMacAddress()));
    }

    @GetMapping("/devices/{macAddress}")
    public DeviceEntryResponse getRegisteredDevice(@PathVariable String macAddress) {
        if(macAddress == null)
            throw new RuntimeException("Invalid mac address");

        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException("device not found");
        }
        return new DeviceEntryResponse(Optional.ofNullable(device.getMacAddress().value()).orElse(""), device.getType());
    }

    @GetMapping("/devices/{macAddress}/topology")
    public DevicesNetworkTopologyResponse getRegisteredDeviceTopology(@PathVariable String macAddress) {
        Device device = devicesNetworkQueryUseCase.getRegisteredDevice(macAddress);
        if(device == null){
            throw new DeviceNotFoundException("device not found");
        }
        return DevicesNetworkTopologyMapper.map(device);
    }

    @GetMapping("/devices/list")
    public List<DeviceEntryResponse> listRegisteredDevices() {
        return devicesNetworkQueryUseCase.getRegisteredDevices().stream()
                .map(d -> new DeviceEntryResponse(d.getMacAddress().value(), d.getType())).collect(Collectors.toList());
    }

    private void validateRegisterDeviceRequest(RegisterDeviceRequest request) {

        if (request.getMacAddress() == null)
            throw new InvalidDeviceRegistrationParameters("invalid mac address");
        if (request.getMacAddress().isEmpty())
            throw new InvalidDeviceRegistrationParameters("invalid mac address");
        if (request.getType() == null)
            throw new InvalidDeviceRegistrationParameters("invalid device type");
    }

    public static final class InvalidDeviceRegistrationParameters extends RuntimeException{

        public InvalidDeviceRegistrationParameters( String message) {
            super(message);
        }
    }
}