package model;

public enum VehicleType {
    CAR,
    BIKE,
    AUTO,
    VAN;

    public static VehicleType getVehicleType(String vehicle) {
        for(VehicleType vehicleType: VehicleType.values()) {
            if(vehicleType.name().equalsIgnoreCase(vehicle)) {
                return vehicleType;
            }
        }
        return null;
    }
}
