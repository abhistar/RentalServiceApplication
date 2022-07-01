package service;

import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService() {
        branchRepository = new BranchRepository();
    }

    public boolean addBranch(String branchName, List<VehicleType> vehicleTypeList) {
        HashMap<VehicleType, List<Vehicle>> vehicleCatalog = new HashMap<>();

        vehicleTypeList.forEach(vehicleType -> {
            vehicleCatalog.put(vehicleType, new ArrayList<>());
        });

        Branch branch = Branch.builder()
                .name(branchName)
                .vehicleCatalog(vehicleCatalog)
                .build();

        return BranchRepository.saveBranch(branch);
    }

    //TODO: delete branch functionality
}
