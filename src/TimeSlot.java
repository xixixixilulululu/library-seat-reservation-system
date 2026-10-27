public class TimeSlot implements Comparable<TimeSlot> {
    private int startTime;
    private int endTime;
    private String studentId;
    public TimeSlot(int startTime, int endTime, String studentId) {
        if (startTime >= endTime) {
            throw new IllegalArgumentException(" The start time must be earlier than the end time ");
        }
        if (startTime < 0 || endTime > 24) {
            throw new IllegalArgumentException(" The time must be within the range of 0-24 hours ");
        }
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException(" Student ID must not be blank ");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentId = studentId;
    }

    public boolean overlaps(TimeSlot other) {
        return !(this.endTime <= other.startTime || this.startTime >= other.endTime);
    }

    public boolean overlaps(int start, int end) {
        return !(this.endTime <= start || this.startTime >= end);
    }

    @Override
    public int compareTo(TimeSlot other) {
        if (this.startTime != other.startTime) {
            return Integer.compare(this.startTime, other.startTime);
        }
        return Integer.compare(this.endTime, other.endTime);
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return String.format("[%02d:00 - %02d:00, student: %s]", startTime, endTime, studentId);
    }
}
