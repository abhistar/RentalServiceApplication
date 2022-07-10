package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import repository.BranchRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingServiceTest {
    BookingService bookingService = new BookingService();

    @Mock
    BranchRepository branchRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookingService, "branchRepository", branchRepository);
    }

    @Nested
    class BookVehicleMethod {
        @Test
        void shouldReturnNegative1_WhenBranchHasNoCarAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            queue.add(Mockito.mock(Vehicle.class));
            Branch branch = Branch.builder()
                .name("B1")
                .vehicleCatalog(new HashMap<>(Map.of(CAR, queue)))
                .build();

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            try (MockedStatic<VehicleService> mockedStatic = mockStatic(VehicleService.class)) {
                mockedStatic.when(() -> VehicleService.isVehicleAvailable(any(), anyInt(), anyInt())).thenReturn(false);

                assertEquals(-1, bookingService.bookVehicle(branch.getName(), CAR, 1, 5));

                mockedStatic.verify(() -> VehicleService.isVehicleAvailable(any(), anyInt(), anyInt()), times(1));
                mockedStatic.verify(() -> VehicleService.bookVehicle(any(), anyInt(), anyInt()), times(0));
            }
        }

        @Test
        void shouldExecuteBookVehicle1_timeWhenBranchHas2CarsAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            queue.add(Mockito.mock(Vehicle.class));
            queue.add(Mockito.mock(Vehicle.class));
            Branch branch = Branch.builder()
                .name("B1")
                .vehicleCatalog(new HashMap<>(Map.of(CAR, queue)))
                .build();

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            try (MockedStatic<VehicleService> mockedStatic = mockStatic(VehicleService.class)) {
                mockedStatic.when(() -> VehicleService.isVehicleAvailable(any(), anyInt(), anyInt())).thenReturn(true);

                assertEquals(0.0, bookingService.bookVehicle(branch.getName(), CAR, 1, 5));

                mockedStatic.verify(() -> VehicleService.isVehicleAvailable(any(), anyInt(), anyInt()), times(1));
                mockedStatic.verify(() -> VehicleService.bookVehicle(any(), anyInt(), anyInt()), times(1));
            }
        }
    }

}