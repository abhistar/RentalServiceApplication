package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeSlot {
    private int startTime;
    private int endTime;
}
