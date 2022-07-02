package repository;

import model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class VehicleRepository {
    private static final Map<String, Vehicle> vehicleMap = new HashMap<>();

    public static boolean saveVehicle(Vehicle vehicle){
        vehicleMap.put(vehicle.getId(), vehicle);
        return true;
    }

    public static Vehicle getVehicleById(String vehicleId){
        return vehicleMap.get(vehicleId);
    }
}
