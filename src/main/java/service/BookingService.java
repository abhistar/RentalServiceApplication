package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.PriorityQueue;

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
}
