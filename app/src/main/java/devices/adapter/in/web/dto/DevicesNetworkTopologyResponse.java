package devices.adapter.in.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DevicesNetworkTopologyResponse implements Serializable{

    private String macAddress;

    private List<DevicesNetworkTopologyResponse> connectedDevices = new ArrayList<>();

    public DevicesNetworkTopologyResponse() {
    }

    public DevicesNetworkTopologyResponse(String macAddress, List<DevicesNetworkTopologyResponse> connectedDevices) {
        this.macAddress = macAddress;
        this.connectedDevices = connectedDevices;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevicesNetworkTopologyResponse other = (DevicesNetworkTopologyResponse) obj;
        if (macAddress == null) {
            if (other.macAddress != null)
                return false;
        } else if (!macAddress.equals(other.macAddress))
            return false;
        return true;
    }

    public List<DevicesNetworkTopologyResponse> getConnectedDevices() {
        return connectedDevices;
    }

    public void setConnectedDevices(List<DevicesNetworkTopologyResponse> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
