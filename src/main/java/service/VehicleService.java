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

    public VehicleService() {
        vehicleRepository = new VehicleRepository();
    }

    public Boolean addVehicle(String branchName, VehicleType vehicleType, String vehicleId, double price) {
        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .type(vehicleType)
                .bookingPrice(price)
                .capacity(4) //TODO add vehicle capacity
                .bookingSchedule(new LinkedList<>())
                .build();

        Branch branch = BranchRepository.getBranchByName(branchName);

        if(!branch.getVehicleCatalog().containsKey(vehicleType))
            return false;

        return vehicleRepository.saveVehicle(vehicle);
    }

    //TODO Add logic to this function
    public static Boolean isVehicleAvailable(Vehicle vehicle) {
        return true;
    }

    //TODO Add logic to this function
    public static Double bookVehicle(Vehicle vehicle, int startTime, int endTime) {
        return vehicle.getBookingPrice();
    }
}
