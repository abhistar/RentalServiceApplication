package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import repository.BranchRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static model.VehicleType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BranchServiceTest {
    BranchService branchService = new BranchService();

    @Mock
    BranchRepository branchRepository;

    @Captor
    ArgumentCaptor<Branch> branchArgumentCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(branchService, "branchRepository", branchRepository);
    }

    @Nested
    class AddBranchMethod {
        @Test
        void shouldBeTrueWhenBranchB1_IsAddedWithCarVehicleType() {
            String branchName = "B1";

            when(branchRepository.saveBranch(any())).thenReturn(true);

            assertTrue(branchService.addBranch(branchName, List.of(CAR)));

            verify(branchRepository, times(1)).saveBranch(branchArgumentCaptor.capture());
            Branch branch = branchArgumentCaptor.getValue();
            assertEquals(branchName, branch.getName());
            assertEquals(1, branch.getVehicleCatalog().size());
            assertTrue(branch.getVehicleCatalog().containsKey(CAR));
        }

        @Test
        void shouldHaveTwoVehicleTypesWhenBranchB1_IsAddedWithCarAndBikeVehicleTypes() {
            String branchName = "B1";

            when(branchRepository.saveBranch(any())).thenReturn(true);

            assertTrue(branchService.addBranch(branchName, List.of(CAR, BIKE)));

            verify(branchRepository, times(1)).saveBranch(branchArgumentCaptor.capture());
            Branch branch = branchArgumentCaptor.getValue();
            assertEquals(branchName, branch.getName());
            assertEquals(2, branch.getVehicleCatalog().size());
            assertTrue(branch.getVehicleCatalog().containsKey(CAR));
            assertTrue(branch.getVehicleCatalog().containsKey(BIKE));
        }

        @Test
        void shouldSaveTwoBranchesWithDifferentBranchNamesB1_AndB2() {
            String firstBranchName = "B1";
            String secondBranchName = "B2";

            when(branchRepository.saveBranch(any())).thenReturn(true);

            assertTrue(branchService.addBranch(firstBranchName, List.of(CAR, BIKE)));
            assertTrue(branchService.addBranch(secondBranchName, List.of(CAR, AUTO, BIKE)));

            verify(branchRepository, times(2)).saveBranch(branchArgumentCaptor.capture());
            List<Branch> branchList = branchArgumentCaptor.getAllValues();
            assertEquals(firstBranchName, branchList.get(0).getName());
            assertEquals(2, branchList.get(0).getVehicleCatalog().size());
            assertEquals(secondBranchName, branchList.get(1).getName());
            assertEquals(3, branchList.get(1).getVehicleCatalog().size());
        }

        @Test
        void shouldReturnFalseWhenB1_IsSavedWithNoVehicleTypes() {
            String branchName = "B1";

            assertFalse(branchService.addBranch(branchName, List.of()));

            verify(branchRepository, times(0)).saveBranch(branchArgumentCaptor.capture());
        }
    }
}