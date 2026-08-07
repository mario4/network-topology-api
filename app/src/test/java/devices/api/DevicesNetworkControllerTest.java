package devices.api;

import devices.adapter.in.web.dto.DeviceEntryResponse;
import devices.adapter.in.web.dto.DevicesNetworkTopologyResponse;
import devices.adapter.in.web.dto.ErrorResponse;
import devices.adapter.in.web.dto.RegisterDeviceRequest;
import devices.domain.DeviceType;
import devices.port.out.DevicesNetworkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static devices.common.TestDataUtil.givenMacAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIterable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DevicesNetworkControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DevicesNetworkRepository devicesNetworkRepository;

    private String baseUrl;

    @BeforeEach
    void reset() {
        baseUrl = "http://localhost:" + port + "/api/network/devices";
        devicesNetworkRepository.clear();
    }

    // Registration tests

    @Test
    void shouldRegisterNewDevice() {
        String macAddress = givenMacAddress("01");

        sendRegisterRequest(baseUrl, macAddress, DeviceType.SWITCH, "");

        String urlGetDevice = baseUrl + "/" + macAddress;
        DeviceEntryResponse response = restTemplate.getForObject(urlGetDevice, DeviceEntryResponse.class);

        assertThat(response.macAddress()).isEqualTo(macAddress);
    }

    @Test
    void should_Validate_Device_Registration_Parameters() {
        ResponseEntity<ErrorResponse> response;

        response = sendRegisterRequest(baseUrl, "", null, "", ErrorResponse.class);
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(400));

        assertThat(response.getBody().message()).contains(RegisterDeviceRequest.MAC_ADDRESS_REQUIRED).contains(RegisterDeviceRequest.TYPE_REQUIRED);
    }

    @Test
    void shouldReturnRegisteredDevice() {
        String macAddress = givenMacAddress("01");

        sendRegisterRequest(baseUrl, macAddress, DeviceType.SWITCH, "");

        String urlGetDevice = baseUrl + "/" + macAddress;
        DeviceEntryResponse response = restTemplate.getForObject(urlGetDevice, DeviceEntryResponse.class);

        assertThat(response.macAddress()).isEqualTo(macAddress);
    }

    @Test
    void can_List_Registered_Devices() {

        String[] macAddresses = new String[]{givenMacAddress("01"), givenMacAddress("02"), givenMacAddress("03")};

        Arrays.stream(macAddresses).forEach((s) -> sendRegisterRequest(baseUrl, s, DeviceType.GATEWAY, ""));

        ResponseEntity<List<DeviceEntryResponse>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assertThatIterable(
                response.getBody().stream().map(DeviceEntryResponse::macAddress).collect(Collectors.toList()))
                .contains(macAddresses);
    }

    @Test
    void can_Return_Network_Topology() {
        setupBranchingNetworkTopology(baseUrl);

        String urlGetDevice = baseUrl + "/topology";

        DevicesNetworkTopologyResponse response = restTemplate.getForObject(urlGetDevice, DevicesNetworkTopologyResponse.class);

        assertThat(response.getConnectedDevices().size()).isEqualTo(1);
        assertThat(response.getConnectedDevices().stream().findFirst().get().getConnectedDevices().size())
                .isEqualTo(3);

    }

    @Test
    void can_Return_Network_Topology_Starting_From_Internal_Device() {

        setupBranchingNetworkTopology(baseUrl);

        String urlGetDevice = baseUrl + "/" + givenMacAddress("03") + "/topology";

        DevicesNetworkTopologyResponse response = restTemplate.getForObject(urlGetDevice, DevicesNetworkTopologyResponse.class);

        assertThat(response.getConnectedDevices().size())
                .isEqualTo(2);

    }

    private <T> ResponseEntity<T> sendRegisterRequest(
            String url, String macAddr, DeviceType deviceType, String uplinkMacAddr, Class<T> responseType) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RegisterDeviceRequest deviceRequest = new RegisterDeviceRequest(macAddr, deviceType, uplinkMacAddr);
        HttpEntity<RegisterDeviceRequest> httpEntity = new HttpEntity<>(deviceRequest, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType);
    }

    private void sendRegisterRequest(
            String url, String macAddr, DeviceType deviceType, String uplinkMacAddr) {

        sendRegisterRequest(url, macAddr, deviceType, uplinkMacAddr, Void.class);
    }

    private void setupBranchingNetworkTopology(String url) {
        sendRegisterRequest(url, givenMacAddress("01"), DeviceType.SWITCH, "");
        sendRegisterRequest(url, givenMacAddress("02"), DeviceType.GATEWAY, givenMacAddress("01"));
        sendRegisterRequest(url, givenMacAddress("03"), DeviceType.GATEWAY, givenMacAddress("01"));
        sendRegisterRequest(url, givenMacAddress("04"), DeviceType.GATEWAY, givenMacAddress("01"));
        sendRegisterRequest(url, givenMacAddress("05"), DeviceType.GATEWAY, givenMacAddress("03"));
        sendRegisterRequest(url, givenMacAddress("06"), DeviceType.GATEWAY, givenMacAddress("03"));
    }
}
