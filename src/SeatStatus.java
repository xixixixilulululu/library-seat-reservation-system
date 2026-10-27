// Seat status class - used for stack management offhand functionality

public class SeatStatus {
    private String status;  // "available", "occupied", "temporarily_away"
    private String studentId;
    private long timestamp;  // Timestamp of status change
    private long returnTime;  // Expected return time (only for the temporarily_away state)

    public SeatStatus(String status, String studentId, long timestamp) {
        this.status = status;
        this.studentId = studentId;
        this.timestamp = timestamp;
        this.returnTime = 0;
    }

    public SeatStatus(String status, String studentId, long timestamp, long returnTime) {
        this.status = status;
        this.studentId = studentId;
        this.timestamp = timestamp;
        this.returnTime = returnTime;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentId() {
        return studentId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public boolean isExpired() {
        if ("temporarily_away".equals(status) && returnTime > 0) {
            return System.currentTimeMillis() > returnTime;
        }
        return false;
    }

    @Override
    public String toString() {
        if ("temporarily_away".equals(status)) {
            long remainingMinutes = (returnTime - System.currentTimeMillis()) / (60 * 1000);
            return String.format(" Status: temporarily away, student: %s, Remaining time: %d minutes",studentId, Math.max(0, remainingMinutes));
        }
        return String.format(" status: %s, Student: %s", status, studentId);
    }
}
