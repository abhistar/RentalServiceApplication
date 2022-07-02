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

    public Double bookVehicle(String branchName, VehicleType vehicleType, int startTime, int endTime) {
        Branch branch = branchRepository.getBranchByName(branchName);

        for (Vehicle vehicle : branch.getVehicleCatalog().get(vehicleType)) {
            if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                return VehicleService.bookVehicle(vehicle, startTime, endTime);
            }
        }

        return -1.0;
    }

    public String displayVehicle(String branchName, int startTime, int endTime) {
        Branch branch = branchRepository.getBranchByName(branchName);
        PriorityQueue<Vehicle> availableVehicles = new PriorityQueue<Vehicle>(10, new VehicleByPriceComparator());

        for (VehicleType vehicleType: branch.getVehicleCatalog().keySet()) {
            for (Vehicle vehicle: branch.getVehicleCatalog().get(vehicleType)) {
                if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                    availableVehicles.add(vehicle);
                }
            }
        }

        return getVehicleIdStringFromQueue(availableVehicles);
    }

    private String getVehicleIdStringFromQueue(PriorityQueue<Vehicle> availableVehicles) {
        String vehiclesList = Objects.requireNonNull(availableVehicles.poll()).getId();
        while (!availableVehicles.isEmpty()) {
            vehiclesList = MessageFormat.format("{0}, {1}", vehiclesList, Objects.requireNonNull(availableVehicles.poll()).getId());
        }
        return vehiclesList;
    }

    //TODO: delete branch functionality
}
