package hu.szte.ovrt.batterylifetime.task.data;

import java.time.LocalDateTime;

public class RunTime {
    private long ms;
    private LocalDateTime date;
    private boolean local;

    public RunTime() {
    }

    public RunTime(long ms, LocalDateTime date, boolean local) {
        this.ms = ms;
        this.date = date;
        this.local = local;
    }

    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        this.ms = ms;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
