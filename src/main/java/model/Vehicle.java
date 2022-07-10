package model;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class Vehicle {
    private String id;         // unique identifier of vehicle
    private VehicleType type;    // type of vehicle like car, bike, auto etc.
    private double bookingPrice; //fixed rent of vehicle
    private LinkedList<TimeSlot> bookingSchedule;
    private int capacity; // seating capacity of vehicle

}
