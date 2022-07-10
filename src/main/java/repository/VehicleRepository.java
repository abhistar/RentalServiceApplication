package repository;

import model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class VehicleRepository {
    private static final Map<String, Vehicle> vehicleMap = new HashMap<>();

    public boolean saveVehicle(Vehicle vehicle){
        if(vehicleMap.containsKey(vehicle.getId())) return false;
        
        vehicleMap.put(vehicle.getId(), vehicle);
        return true;
    }

    public Vehicle getVehicleById(String vehicleId){
        return vehicleMap.get(vehicleId);
    }
}
