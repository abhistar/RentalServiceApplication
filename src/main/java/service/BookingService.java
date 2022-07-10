package service;

import comparator.VehicleByPriceComparator;
import model.Branch;
import model.Vehicle;
import model.VehicleType;
import repository.BranchRepository;
import util.DecimalUtil;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.PriorityQueue;

public class BookingService {
    private final BranchRepository branchRepository;

    public BookingService() {
        branchRepository = new BranchRepository();
    }

    public Double bookVehicle(String branchName, VehicleType vehicleType, int startTime, int endTime) {
        Branch branch = branchRepository.getBranchByName(branchName);

        int countOccupancy = 0;
        int totalVehicleByType = branch.getVehicleCatalog().get(vehicleType).size();

        for (Vehicle vehicle : branch.getVehicleCatalog().get(vehicleType)) {
            if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                return DecimalUtil.roundDouble(applyDynamicPricing(VehicleService.bookVehicle(vehicle, startTime, endTime),
                    checkDynamicPricing(countOccupancy, totalVehicleByType)));
            }
            countOccupancy++;
        }

        return -1.0;
    }

    public String displayVehicle(String branchName, int startTime, int endTime) {
        Branch branch = branchRepository.getBranchByName(branchName);
        PriorityQueue<Vehicle> availableVehicles = new PriorityQueue<Vehicle>(10, new VehicleByPriceComparator());

        for (VehicleType vehicleType: branch.getVehicleCatalog().keySet()) {
            for (Vehicle vehicle: branch.getVehicleCatalog().get(vehicleType)) {
                if (VehicleService.isVehicleAvailable(vehicle, startTime, endTime)) {
                    availableVehicles.add(vehicle);
                }
            }
        }

        return getVehicleIdStringFromQueue(availableVehicles);
    }

    private String getVehicleIdStringFromQueue(PriorityQueue<Vehicle> availableVehicles) {
        if(availableVehicles.isEmpty()) return "";
        String vehiclesList = Objects.requireNonNull(availableVehicles.poll()).getId();
        while (!availableVehicles.isEmpty()) {
            vehiclesList = MessageFormat.format("{0}, {1}", vehiclesList, Objects.requireNonNull(availableVehicles.poll()).getId());
        }
        return vehiclesList;
    }

    private boolean checkDynamicPricing(int countOccupancy, int totalVehicleByType) {
        return ((double) countOccupancy / (double) totalVehicleByType) >= 0.8;
    }

    private Double applyDynamicPricing(double bookingPrice, boolean dynamicPricingApplicable) {
        return dynamicPricingApplicable ? 1.1*bookingPrice : bookingPrice;
    }
}
