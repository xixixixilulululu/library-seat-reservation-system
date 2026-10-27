import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


// Seat class - represents a seat in the library
//Core: Each seat maintains an AVL tree to manage all reservation time slots
public class Seat {
    private String seatId;
    private String location;
    private Map<String, Boolean> attributes;
    private AVLTree reservations;
    private Stack<SeatStatus> statusStack;
    private String currentStudentId;

    public Seat(String seatId, String location) {
        this.seatId = seatId;
        this.location = location;
        this.attributes = new HashMap<>();
        this.reservations = new AVLTree();
        this.statusStack = new Stack<>();
        this.currentStudentId = null;

        statusStack.push(new SeatStatus("available", null, System.currentTimeMillis()));
    }

    public void setAttribute(String attribute, boolean value) {
        attributes.put(attribute.toLowerCase(), value);
    }
    public boolean hasAttribute(String attribute) {
        return attributes.getOrDefault(attribute.toLowerCase(), false);
    }
    public boolean isAvailable(int startTime, int endTime) {
        return !reservations.hasConflict(startTime, endTime);
    }
    public boolean reserve(int startTime, int endTime, String studentId) {
        TimeSlot timeSlot = new TimeSlot(startTime, endTime, studentId);
        return reservations.insert(timeSlot);
    }

    public boolean cancelReservation(int startTime, int endTime, String studentId) {
        return reservations.delete(startTime, endTime, studentId);
    }

    public boolean temporarilyLeave(String studentId, int durationMinutes) {
        if (durationMinutes <= 0 || !getCurrentStatus().equals("occupied") ||
            !studentId.equals(currentStudentId)) {
            return false;
        }

        long returnTime = System.currentTimeMillis() + (durationMinutes * 60 * 1000L);
        SeatStatus awayStatus = new SeatStatus("temporarily_away", studentId,
                                              System.currentTimeMillis(), returnTime);
        statusStack.push(awayStatus);
        return true;
    }

    public boolean returnToSeat(String studentId) {
        if (statusStack.isEmpty() || !getCurrentStatus().equals("temporarily_away")) {
            return false;
        }

        SeatStatus topStatus = statusStack.peek();
        if (!topStatus.getStudentId().equals(studentId)) {
            return false;
        }

        statusStack.pop();
        return true;
    }

    public void checkTimeout() {
        if (!statusStack.isEmpty() && statusStack.peek().isExpired()) {
            statusStack.pop();
            if (!statusStack.isEmpty() && "occupied".equals(statusStack.peek().getStatus())) {
                statusStack.pop();
            }
            if (statusStack.isEmpty() || !"available".equals(statusStack.peek().getStatus())) {
                statusStack.push(new SeatStatus("available", null, System.currentTimeMillis()));
            }
            currentStudentId = null;
        }
    }

    public void occupy(String studentId) {
        statusStack.push(new SeatStatus("occupied", studentId, System.currentTimeMillis()));
        currentStudentId = studentId;
    }

    public void release() {
        while (statusStack.size() > 1) {
            statusStack.pop();
        }
        if (statusStack.isEmpty()) {
            statusStack.push(new SeatStatus("available", null, System.currentTimeMillis()));
        }
        currentStudentId = null;
    }

    public String getCurrentStatus() {
        checkTimeout();
        return statusStack.isEmpty() ? "available" : statusStack.peek().getStatus();
    }

    public String getCurrentStudentId() {
        checkTimeout();
        return currentStudentId;
    }

    public int calculateScore(Map<String, Integer> scoringRules, java.util.Set<String> studentPreferences) {
        int score = 0;

        for (String preference : studentPreferences) {
            if (hasAttribute(preference) && scoringRules.containsKey(preference)) {
                score += scoringRules.get(preference);
            }
        }

        if (hasAttribute("near_door")) {
            score -= 1;
        }

        return score;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getLocation() {
        return location;
    }

    public Map<String, Boolean> getAttributes() {
        return new HashMap<>(attributes);
    }

    public AVLTree getReservations() {
        return reservations;
    }

    public Stack<SeatStatus> getStatusStack() {
        Stack<SeatStatus> copy = new Stack<>();
        copy.addAll(statusStack);
        return copy;
    }

     @Override
     public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append(String.format(" seat [ID: %s, location: %s]", seatId, location));
         sb.append("\n attribute: ");
         for (Map.Entry<String, Boolean> entry : attributes.entrySet()) {
             if (entry.getValue()) {
                 sb.append(entry.getKey()).append(" ");
             }
         }
         sb.append("\n current status: ").append(getCurrentStatus());
         if (currentStudentId!= null) {
             sb.append(" (student:").append(currentStudentId).append(")");
         }
         sb.append("\n reservation quantity: ").append(reservations.size());
         return sb.toString();
     }
}
