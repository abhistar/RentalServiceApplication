package model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

@Data
@Builder
public class Branch {
    String name; //unique branch name
    HashMap<VehicleType, PriorityQueue<Vehicle>> vehicleCatalog; //TODO add custom comparator for price based strategy
}
