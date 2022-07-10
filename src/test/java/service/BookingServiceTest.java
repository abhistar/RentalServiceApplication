package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.TimeSlot;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import repository.BranchRepository;

import java.util.*;

import static model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingServiceTest {
    BookingService bookingService = new BookingService();

    Branch branch;

    Vehicle firstVehicle;
    Vehicle secondVehicle;

    @Mock
    BranchRepository branchRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookingService, "branchRepository", branchRepository);

        branch = Branch.builder().name("B1").build();

        firstVehicle = Vehicle.builder().id("C1").type(CAR).bookingPrice(1000.0).build();
        secondVehicle = Vehicle.builder().id("C2").type(CAR).bookingPrice(500.0).build();
    }

    @Nested
    class BookVehicleMethod {
        @Test
        void shouldReturnNegative1_WhenBranchHasNoCarAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals(-1, bookingService.bookVehicle(branch.getName(), CAR, 1, 5));
        }

        @Test
        void shouldReturnPriceOfVehicle1_WhenBranchHasOneCarAvailableAndIsBookedFor1_Hour() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals(1000.0, bookingService.bookVehicle(branch.getName(), CAR, 4, 5));
        }

        @Test
        void shouldReturn4_TimesPriceOfVehicle1_WhenBranchHasOneCarAvailableAndIsBookedFor4_Hour() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals(4000.0, bookingService.bookVehicle(branch.getName(), CAR, 4, 8));
        }

        @Test
        void shouldExecuteBookVehicle1_timeWhenBranchHas1_OutOf2_CarsAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            secondVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(secondVehicle);
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(3).endTime(5).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals(1000.0, bookingService.bookVehicle(branch.getName(), CAR, 1, 2));
        }

        @Test
        void shouldExecuteBookingWithSurgePricingOf10_PercentWhen5thCarOutOf6_CarsIsBooked() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            Vehicle dummyVehicle1 = Vehicle.builder().bookingPrice(200.0)
                .bookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build()))).build();
            Vehicle dummyVehicle2 = Vehicle.builder().bookingPrice(200.0)
                .bookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build()))).build();
            Vehicle dummyVehicle3 = Vehicle.builder().bookingPrice(200.0)
                .bookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build()))).build();
            Vehicle dummyVehicle4 = Vehicle.builder().bookingPrice(200.0)
                .bookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build()))).build();

            queue.add(dummyVehicle1);
            queue.add(dummyVehicle2);
            queue.add(dummyVehicle3);
            queue.add(dummyVehicle4);

            secondVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(secondVehicle);
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(3).endTime(5).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals(1100.0, bookingService.bookVehicle(branch.getName(), CAR, 1, 2));
        }
    }

    @Nested
    class DisplayVehicleMethod {
        @Test
        void shouldReturnC1_AsStringOutputWhenHasOneVehicleAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(3).endTime(5).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals("C1", bookingService.displayVehicle(branch.getName(), 1, 2));
        }

        @Test
        void shouldReturnEmptyStringOutputWhenHasNoVehicleAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(3).endTime(5).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals("", bookingService.displayVehicle(branch.getName(), 1, 4));
        }

        @Test
        void shouldReturnV2comma_V1_AsStringOutputWhenHasOneVehicleAvailable() {
            PriorityQueue<Vehicle> queue = new PriorityQueue<>(5, new VehicleByPriceComparator());
            secondVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(1).endTime(2).build())));
            queue.add(secondVehicle);
            firstVehicle.setBookingSchedule(new LinkedList<>(List.of(TimeSlot.builder().startTime(3).endTime(5).build())));
            queue.add(firstVehicle);
            branch.setVehicleCatalog(new HashMap<>(Map.of(CAR, queue)));

            when(branchRepository.getBranchByName(any())).thenReturn(branch);

            assertEquals("C2, C1", bookingService.displayVehicle(branch.getName(), 5, 11));

        }
    }
}