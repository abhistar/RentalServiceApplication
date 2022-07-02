package model;

public enum VehicleType {
    CAR(4),
    BIKE(2),
    AUTO(3),
    VAN(6);

    private final int capacity;

    private VehicleType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public static VehicleType getVehicleType(String vehicle) {
        for(VehicleType vehicleType: VehicleType.values()) {
            if(vehicleType.name().equalsIgnoreCase(vehicle)) {
                return vehicleType;
            }
        }
        return null;
    }
}
