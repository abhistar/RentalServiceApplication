package repository;

import model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class VehicleRepository {
    private static Map<Integer, Vehicle> vehicleMap = new HashMap<>();

    public static boolean saveVehicle(Vehicle vehicle){
        vehicleMap.put(vehicle.getId(), vehicle);
        return true;
    }

    public static Vehicle getVehicleById(int vehicleId){
        return vehicleMap.get(vehicleId);
    }
}
