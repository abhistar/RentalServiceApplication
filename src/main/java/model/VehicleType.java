package model;

public enum VehicleType {
    CAR(4),
    BIKE(2),
    AUTO(3),
    VAN(6),
    BUS(25);

    private final int capacity;

    private VehicleType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public static VehicleType getVehicleType(String vehicleTypeString) {
        for(VehicleType vehicleType: VehicleType.values()) {
            if(vehicleType.name().equalsIgnoreCase(vehicleTypeString)) {
                return vehicleType;
            }
        }
        return null;
    }
}
