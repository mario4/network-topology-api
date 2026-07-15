package devices.network;

import devices.adapter.out.InMemoryDevicesNetworkRepository;
import devices.application.RegisterDeviceCommand;
import devices.application.RegisterDeviceUseCase;
import devices.domain.DeviceType;
import devices.domain.DevicesNetwork;
import devices.port.out.DevicesNetworkRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentDeviceRegistrationTest {

    private static final int nThreads = 10;

    private static final ExecutorService
            fixedThreadPool = Executors.newFixedThreadPool(nThreads);


    private final DevicesNetworkRepository devicesNetworkRepository = new InMemoryDevicesNetworkRepository();

    private final RegisterDeviceUseCase registerDeviceUseCase = new RegisterDeviceUseCase(devicesNetworkRepository);

    @BeforeEach
    void setUp() {
        setupInitialNetworkState();
    }

    private void setupInitialNetworkState() {
        devicesNetworkRepository.clear();
        // create uplink device previous to concurrent registrations
        RegisterDeviceCommand command = new RegisterDeviceCommand("00", DeviceType.GATEWAY, "");
        registerDeviceUseCase.execute(command);
    }

    @Test
    public void shouldRegisterMultipleConcurrentDevicesSafely() {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch stopLatch = new CountDownLatch(10);

        List<RegisterDeviceCommand> commands = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            RegisterDeviceCommand command = new RegisterDeviceCommand("00" + i, DeviceType.GATEWAY, "00");
            commands.add(command);
        }

        commands.forEach(registerDeviceUseCase::execute);
        DevicesNetwork expectedNetwork = devicesNetworkRepository.load();

        setupInitialNetworkState();

        try {

            commands.forEach(c-> {
                fixedThreadPool.execute(() -> {
                    try {
                        startLatch.await();

                        registerDeviceUseCase.execute(c);

                        stopLatch.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            startLatch.countDown();

            stopLatch.await();

            fixedThreadPool.shutdownNow();

            DevicesNetwork devicesNetwork = devicesNetworkRepository.load();
            Assertions.assertThat(expectedNetwork.getRegisteredDevices()).hasSameElementsAs(devicesNetwork.getRegisteredDevices());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
