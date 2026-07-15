package devices.domain;

import devices.domain.exceptions.CyclicUplinkReferenceException;
import devices.domain.exceptions.DuplicateDeviceException;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class DevicesNetwork {

    private final Set<Device> registeredDevices = Collections.synchronizedSet(new LinkedHashSet<>());

    private final Device networkRoot = new Device("root", null, null);

    public void registerDevice(Device newDevice) {
        Device device = getRegisteredDevice(newDevice.getMacAddress().value());

        if (device != null) {
            throw new DuplicateDeviceException();
        }

        if (newDevice.getMacAddress().equals(newDevice.getUplinkMacAddress()))
            throw new CyclicUplinkReferenceException();

        deployDeviceToNetwork(newDevice);
    }

    public Device getRegisteredDevice(String string) {
        synchronized (registeredDevices) {
           return registeredDevices.stream().filter(d -> d.getMacAddress().value().equals(string))
                    .findFirst()
                    .orElse(null);
        }
    }

    public Set<Device> getRegisteredDevices() {
        synchronized (registeredDevices) {
            return registeredDevices.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public Device getTopology() {
        return networkRoot;
    }

    private void deployDeviceToNetwork(Device device) {
        if (isUplinkEmpty(device)) {
            networkRoot.getConnectedDevices().add(device);
        } else {
            Device uplinkDevice = getRegisteredDevice(device.getUplinkMacAddress().value());

            if (uplinkDevice == null) {
                networkRoot.getConnectedDevices().add(device);
            } else {
                if (createsCyclicReference(device, uplinkDevice)) {
                    throw new CyclicUplinkReferenceException();
                }
                uplinkDevice.getConnectedDevices().add(device);
            }
        }

        resolveOutOfOrderUplinkConnections(device);
        registeredDevices.add(device);
    }

    private boolean createsCyclicReference(Device newDevice, Device uplinkDevice) {

        return newDevice.getMacAddress().equals(uplinkDevice.getUplinkMacAddress());
    }

    private boolean isUplinkEmpty(Device device) {
        return device.getUplinkMacAddress().value() == null || device.getUplinkMacAddress().value().isEmpty();
    }

    private void resolveOutOfOrderUplinkConnections(Device device) {
        Device toRemove = null;
        for (Iterator<Device> i = networkRoot.getConnectedDevices().iterator(); i.hasNext();) {
            Device hangingDevice = i.next();
            if (isNoLongerHangingDevice(device.getMacAddress().value(), hangingDevice.getUplinkMacAddress().value())) {
                device.getConnectedDevices().add(hangingDevice);
                toRemove=hangingDevice;
            }
        }
        if (toRemove!=null)
            networkRoot.getConnectedDevices().remove(toRemove);
    }

    private boolean isNoLongerHangingDevice(String device, String UplinkMacAddress) {
        return Optional.ofNullable(UplinkMacAddress).filter(addr -> addr.equals(device)).isPresent();
    }
}
