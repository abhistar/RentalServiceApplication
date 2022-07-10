package service;

import model.Branch;
import model.TimeSlot;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;
import repository.VehicleRepository;

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
                .capacity(vehicleType.getCapacity())
                .bookingSchedule(new LinkedList<>())
                .build();

        Branch branch = branchRepository.getBranchByName(branchName);

        if(!branch.getVehicleCatalog().containsKey(vehicleType))
            return false;

        branch.getVehicleCatalog().get(vehicleType).add(vehicle);
        return vehicleRepository.saveVehicle(vehicle);
    }

    public static Boolean isVehicleAvailable(Vehicle vehicle, int startTime, int endTime) {
        List<TimeSlot> vehicleBookingSchedules = vehicle.getBookingSchedule();
        if(vehicleBookingSchedules.size() == 0) {
            return true;
        }
        int slotNumber = getSlotNumberOfNextBookedSlot(vehicleBookingSchedules, startTime);

        return (slotNumber == vehicleBookingSchedules.size() && startTime >= vehicleBookingSchedules.get(slotNumber-1).getEndTime())
            || (slotNumber <vehicleBookingSchedules.size() && endTime <= vehicleBookingSchedules.get(slotNumber).getStartTime());
    }

    public static Double bookVehicle(Vehicle vehicle, int startTime, int endTime) {
        List<TimeSlot> vehicleBookingSchedules = vehicle.getBookingSchedule();
        int slotNumber = getSlotNumberOfNextBookedSlot(vehicleBookingSchedules, startTime);

        vehicle.getBookingSchedule().add(slotNumber, TimeSlot.builder().startTime(startTime).endTime(endTime).build());
        return vehicle.getBookingPrice()*(endTime - startTime);
    }

    private static int getSlotNumberOfNextBookedSlot(List<TimeSlot> vehicleBookingSchedule, int startTime) {
        for(int slotNumber = 0; slotNumber < vehicleBookingSchedule.size(); slotNumber++) {
            if(startTime < vehicleBookingSchedule.get(slotNumber).getStartTime()) return slotNumber;
        }
        return vehicleBookingSchedule.size();
    }
}
