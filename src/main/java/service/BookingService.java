package service;

import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;

public class BookingService {
    private final BranchRepository branchRepository;

    public BookingService() {
        branchRepository = new BranchRepository();
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
}
