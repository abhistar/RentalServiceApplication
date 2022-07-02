package comparator;

import model.Vehicle;

import java.util.Comparator;

public class VehicleByPriceComparator implements Comparator<Vehicle> {
    public int compare(Vehicle vehicle, Vehicle otherVehicle) {
        if (vehicle.getBookingPrice() < otherVehicle.getBookingPrice()) return -1;
        else if (vehicle.getBookingPrice() > otherVehicle.getBookingPrice()) return 1;
        return 0;
    }
}
