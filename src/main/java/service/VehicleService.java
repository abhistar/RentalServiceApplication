package service;

import model.Branch;
import model.TimeSlot;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;
import repository.VehicleRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;

    public VehicleService() {
        vehicleRepository = new VehicleRepository();
        branchRepository = new BranchRepository();
    }

    public Boolean addVehicle(String branchName, VehicleType vehicleType, String vehicleId, double price) {
        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .type(vehicleType)
                .bookingPrice(price)
                .capacity(4) //TODO add vehicle capacity
                .bookingSchedule(new LinkedList<>())
                .build();

        Branch branch = branchRepository.getBranchByName(branchName);

        if(!branch.getVehicleCatalog().containsKey(vehicleType))
            return false;

        return vehicleRepository.saveVehicle(vehicle);
    }

    public static Boolean isVehicleAvailable(Vehicle vehicle) {
        return true;
    }

    public static Double bookVehicle(Vehicle vehicle, int startTime, int endTime) {
        return vehicle.getBookingPrice();
    }
}
