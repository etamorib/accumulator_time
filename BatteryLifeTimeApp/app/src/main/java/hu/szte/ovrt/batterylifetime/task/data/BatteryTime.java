package hu.szte.ovrt.batterylifetime.task.data;

import java.time.LocalDateTime;

public class BatteryTime {
    private double percentage;
    private LocalDateTime date;

    public BatteryTime() {
    }

    public BatteryTime(double percentage, LocalDateTime date) {
        this.percentage = percentage;
        this.date = date;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
