package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;

import java.text.MessageFormat;
import java.util.*;

public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService() {
        branchRepository = new BranchRepository();
    }

    public Boolean addBranch(String branchName, List<VehicleType> vehicleTypeList) {
        HashMap<VehicleType, PriorityQueue<Vehicle>> vehicleCatalog = new HashMap<>();

        vehicleTypeList.forEach(vehicleType -> {
            vehicleCatalog.put(vehicleType, new PriorityQueue<>(5, new VehicleByPriceComparator()));
        });

        if (vehicleCatalog.isEmpty()) return false;

        Branch branch = Branch.builder()
                .name(branchName)
                .vehicleCatalog(vehicleCatalog)
                .build();

        return branchRepository.saveBranch(branch);
    }
}
