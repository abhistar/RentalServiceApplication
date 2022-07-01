package model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class Branch {
    String name; //unique branch name
    HashMap<VehicleType, List<Vehicle>> vehicleCatalog;
}
