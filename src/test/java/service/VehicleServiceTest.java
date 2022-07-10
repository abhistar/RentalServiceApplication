package service;

import model.Branch;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import repository.BranchRepository;
import repository.VehicleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static model.VehicleType.BIKE;
import static model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleServiceTest {
    VehicleService vehicleService = new VehicleService();

    @Mock
    BranchRepository branchRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @Captor
    ArgumentCaptor<Vehicle> vehicleArgumentCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(vehicleService, "branchRepository", branchRepository);
        ReflectionTestUtils.setField(vehicleService, "vehicleRepository", vehicleRepository);
    }

    @Nested
    class AddVehicleMethod {
        @Test
        void shouldBeTrueWhenVehicleIsAddedWithVehicleTypeAllowedInBranch() {
            String branchName = "B1";
            String vehicleId = "V1";
            double price = 500.0;

            Branch branch = Mockito.mock(Branch.class);
            HashMap catalog = Mockito.mock(HashMap.class);
            PriorityQueue queue = Mockito.mock(PriorityQueue.class);

            when(branchRepository.getBranchByName(any())).thenReturn(branch);
            when(branch.getVehicleCatalog()).thenReturn(catalog);
            when(catalog.containsKey(CAR)).thenReturn(true);
            when(catalog.get(any())).thenReturn(queue);
            when(queue.add(any())).thenReturn(true);
            when(vehicleRepository.saveVehicle(any())).thenReturn(true);

            assertTrue(vehicleService.addVehicle(branchName, CAR, vehicleId, price));

            verify(vehicleRepository, times(1)).saveVehicle(vehicleArgumentCaptor.capture());
            Vehicle vehicle = vehicleArgumentCaptor.getValue();
            assertEquals(vehicleId, vehicle.getId());
            assertEquals(price, vehicle.getBookingPrice());
            assertEquals(CAR, vehicle.getType());
            assertEquals(4, vehicle.getCapacity());
        }

        @Test
        void shouldSaveTwoVehiclesOfDifferentVehicleTypeInBranch() {
            String branchName = "B1";
            String firstVehicleId = "V1";
            String secondVehicleId = "V2";
            double firstVehiclePrice = 500.0;
            double secondVehiclePrice = 100.0;

            Branch branch = mock(Branch.class);
            HashMap catalog = mock(HashMap.class);
            PriorityQueue queue = mock(PriorityQueue.class);

            when(branchRepository.getBranchByName(any())).thenReturn(branch);
            when(branch.getVehicleCatalog()).thenReturn(catalog);
            when(catalog.containsKey(any())).thenReturn(true);
            when(catalog.get(any())).thenReturn(queue);
            when(queue.add(any())).thenReturn(true);
            when(vehicleRepository.saveVehicle(any())).thenReturn(true);

            assertTrue(vehicleService.addVehicle(branchName, CAR, firstVehicleId, firstVehiclePrice));
            assertTrue(vehicleService.addVehicle(branchName, BIKE, secondVehicleId, secondVehiclePrice));

            verify(vehicleRepository, times(2)).saveVehicle(vehicleArgumentCaptor.capture());
            List<Vehicle> vehicleList = vehicleArgumentCaptor.getAllValues();
            assertEquals(firstVehicleId, vehicleList.get(0).getId());
            assertEquals(firstVehiclePrice, vehicleList.get(0).getBookingPrice());
            assertEquals(CAR, vehicleList.get(0).getType());
            assertEquals(secondVehicleId, vehicleList.get(1).getId());
            assertEquals(secondVehiclePrice, vehicleList.get(1).getBookingPrice());
            assertEquals(BIKE, vehicleList.get(1).getType());
        }

        @Test
        void shouldBeFalseWhenVehicleIsAddedWithVehicleTypeNotAllowedInBranch() {
            String branchName = "B1";
            String vehicleId = "V1";
            double price = 500.0;

            Branch branch = Mockito.mock(Branch.class);
            HashMap catalog = Mockito.mock(HashMap.class);

            when(branchRepository.getBranchByName(any())).thenReturn(branch);
            when(branch.getVehicleCatalog()).thenReturn(catalog);
            when(catalog.containsKey(CAR)).thenReturn(false);

            assertFalse(vehicleService.addVehicle(branchName, CAR, vehicleId, price));

            verify(vehicleRepository, times(0)).saveVehicle(vehicleArgumentCaptor.capture());
        }
    }


}