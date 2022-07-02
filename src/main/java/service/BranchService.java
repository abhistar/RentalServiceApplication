package service;

import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;

import java.util.*;

public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService() {
        branchRepository = new BranchRepository();
    }

    public Boolean addBranch(String branchName, List<VehicleType> vehicleTypeList) {
        HashMap<VehicleType, PriorityQueue<Vehicle>> vehicleCatalog = new HashMap<>();

        vehicleTypeList.forEach(vehicleType -> {
            vehicleCatalog.put(vehicleType, new PriorityQueue<>());
        });

        Branch branch = Branch.builder()
                .name(branchName)
                .vehicleCatalog(vehicleCatalog)
                .build();

        return branchRepository.saveBranch(branch);
    }

    public Double bookVehicle(String branchName, VehicleType vehicleType, int startTime, int endTime) {
        Branch branch = BranchRepository.getBranchByName(branchName);

        for (Vehicle vehicle : branch.getVehicleCatalog().get(vehicleType)) {
            if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                return VehicleService.bookVehicle(vehicle, startTime, endTime);
            }
        }

        return -1.0;
    }

    public String displayVehicle(String branchName, int startTime, int endTime) {
        Branch branch = BranchRepository.getBranchByName(branchName);
        List<String> availableVehicles = new ArrayList<>();

        for (VehicleType vehicleType: branch.getVehicleCatalog().keySet()) {
            for (Vehicle vehicle: branch.getVehicleCatalog().get(vehicleType)) {
                if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                    availableVehicles.add(vehicle.getId());
                }
            }
        }
        return String.join(", ", availableVehicles);
    }

    //TODO: delete branch functionality
}
