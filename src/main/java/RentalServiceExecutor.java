import model.Vehicle;
import model.VehicleType;
import service.BranchService;
import service.VehicleService;

import java.util.ArrayList;
import java.util.List;

public class RentalServiceExecutor {
    private final VehicleService vehicleService;
    private final BranchService branchService;

    public RentalServiceExecutor() {
        vehicleService = new VehicleService();
        branchService = new BranchService();
    }

    public Object execute(String command) {
        String[] arguments = command.split(" ");

        String methodString = arguments[0];

        switch (methodString) {
            case "ADD_BRANCH":
                return branchService.addBranch(arguments[1], getVehicleList(arguments[2]));
            case "ADD_VEHICLE":
                return vehicleService.addVehicle(arguments[1], VehicleType.getVehicleType(arguments[2]),
                    arguments[3], Double.parseDouble(arguments[4]));
            case "BOOK":
                return branchService.bookVehicle(arguments[1], VehicleType.getVehicleType(arguments[2]),
                    Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
            case "DISPLAY_VEHICLES":
                return branchService.displayVehicle(arguments[1], Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
            default:
                return "Command Unknown";
        }
    }

    private List<VehicleType> getVehicleList(String argument) {
        String[] vehicleList = argument.split(",");

        List<VehicleType> vehicles = new ArrayList<>();

        for(String vehicle: vehicleList) {
            for (VehicleType vehicleType: VehicleType.values()) {
                if(vehicleType.name().equalsIgnoreCase(vehicle)) {
                    vehicles.add(vehicleType);
                }
            }
        }
        return vehicles;
    }
}
