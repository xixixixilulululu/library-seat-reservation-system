import java.util.*;

//Seat Manager - The core control class of the system
//Responsible for functions such as seat management, intelligent recommendation, and global scheduling
public class SeatManager {
    private Map<String, Seat> seatMap; // Hash table: Seat ID -> seat object, O(1) search
    private Map<String, Student> studentMap; // Hash table: Student ID -> Student object
    private Map<String, Integer> scoringRules; // Scoring rules

    public SeatManager() {
        this.seatMap = new HashMap<>();
        this.studentMap = new HashMap<>();
        this.scoringRules = new HashMap<>();
        initializeScoringRules();
    }
    private void initializeScoringRules() {
        scoringRules.put("window", 2);
        scoringRules.put("power_outlet", 3);
        scoringRules.put("quiet", 2);
        scoringRules.put("near_restroom", 1);
        scoringRules.put("near_door", -1);
    }
    public void addSeat(Seat seat) {
        seatMap.put(seat.getSeatId(), seat);
    }
    public void addStudent(Student student) {
        studentMap.put(student.getStudentId(), student);
    }
    public Seat getSeat(String seatId) {
        return seatMap.get(seatId);
    }
    public Student getStudent(String studentId) {
        return studentMap.get(studentId);
    }

    //Core Function 1: Millisecond-level vacancy query and conflict detection
    //Time complexity: O(log n) - Thanks to the AVL tree
    public boolean checkAvailability(String seatId, int startTime, int endTime) {
        Seat seat = seatMap.get(seatId);
        if (seat == null) {
            return false;
        }
        return seat.isAvailable(startTime, endTime);
    }

    public boolean reserveSeat(String seatId, int startTime, int endTime, String studentId) {
        Seat seat = seatMap.get(seatId);
        Student student = studentMap.get(studentId);

        if (seat == null || student == null) {
            return false;
        }

        return seat.reserve(startTime, endTime, studentId);
    }

    public boolean cancelReservation(String seatId, int startTime, int endTime, String studentId) {
        Seat seat = seatMap.get(seatId);
        if (seat == null) {
            return false;
        }
        return seat.cancelReservation(startTime, endTime, studentId);
    }

    // Core Function 2: preference-weighted seat recommendation.
    // Available seats are scored against the student's preferences and then sorted.
    //@param startTime Start time
     //@param endTime End time
     //@param studentId Student ID
     //@param topN returns the topN recommendations
     //@return Recommended Seat List (sorted by score)
    public List<SeatRecommendation> recommendSeats(int startTime, int endTime,
                                                   String studentId, int topN) {
        Student student = studentMap.get(studentId);
        if (student == null || topN <= 0) {
            return new ArrayList<>();
        }

        List<SeatRecommendation> recommendations = new ArrayList<>();

        for (Seat seat : seatMap.values()) {
            if (seat.isAvailable(startTime, endTime)) {
                int score = calculateRecommendationScore(seat, student);
                String reason = generateRecommendationReason(seat, student);
                recommendations.add(new SeatRecommendation(seat, score, reason));
            }
        }
        Collections.sort(recommendations);

        return recommendations.subList(0, Math.min(topN, recommendations.size()));
    }

    // Calculate a weighted preference score.
    private int calculateRecommendationScore(Seat seat, Student student) {
        int score = 0;
        Set<String> preferences = student.getPreferences();

        for (String preference : preferences) {
            if (seat.hasAttribute(preference)) {
                score += scoringRules.getOrDefault(preference, 1);
            }
        }

        if (seat.hasAttribute("near_door") && !student.hasPreference("near_door")) {
            score += scoringRules.get("near_door"); // -1分
        }

        int reservationCount = seat.getReservations().size();
        if (reservationCount < 3) {
            score += 1;
        }

        return Math.max(0, score);
    }
    private String generateRecommendationReason(Seat seat, Student student) {
        StringBuilder reason = new StringBuilder();
        List<String> matchedFeatures = new ArrayList<>();

        for (String preference : student.getPreferences()) {
            if (seat.hasAttribute(preference)) {
                matchedFeatures.add(getFeatureDisplayName(preference));
            }
        }

        if (matchedFeatures.isEmpty()) {
            reason.append("Basic seat");
        } else {
            reason.append("Match: ").append(String.join(", ", matchedFeatures));
        }

        return reason.toString();
    }
    private String getFeatureDisplayName(String feature) {
        Map<String, String> displayNames = new HashMap<>();
        displayNames.put("window", "by window");
        displayNames.put("power_outlet", "with socket ");
        displayNames.put("quiet", "quiet");
        displayNames.put("near_restroom", "near restroom");
        displayNames.put("near_door", "near the door");

        return displayNames.getOrDefault(feature, feature);
    }

    //Core Function 3: Temporary Exit Management (Use Stack)
    public boolean temporarilyLeaveSeat(String seatId, String studentId, int durationMinutes) {
        Seat seat = seatMap.get(seatId);
        if (seat == null || durationMinutes <= 0) {
            return false;
        }
        return seat.temporarilyLeave(studentId, durationMinutes);
    }
    public boolean returnToSeat(String seatId, String studentId) {
        Seat seat = seatMap.get(seatId);
        if (seat == null) {
            return false;
        }
        return seat.returnToSeat(studentId);
    }
    public boolean occupySeat(String seatId, String studentId) {
        Seat seat = seatMap.get(seatId);
        if (seat == null || !"available".equals(seat.getCurrentStatus())) {
            return false;
        }
        seat.occupy(studentId);
        return true;
    }
    public boolean releaseSeat(String seatId, String studentId) {
        Seat seat = seatMap.get(seatId);
        if (seat == null || ! studentId.equals(seat.getCurrentStudentId())) {
            return false;
        }
        seat.release();
        return true;
    }
    public void checkAllTimeouts() {
        for (Seat seat : seatMap.values()) {
            seat.checkTimeout();
        }
    }
    public List<Seat> getAllAvailableSeats(int startTime, int endTime) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seatMap.values()) {
            if (seat.isAvailable(startTime, endTime)) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }
    public List<String> getStudentReservations(String studentId) {
        List<String> reservations = new ArrayList<>();
        for (Seat seat : seatMap.values()) {
            for (TimeSlot slot : seat.getReservations().getAllTimeSlots()) {
                if (slot.getStudentId().equals(studentId)) {
                    reservations.add(String.format(" seat %s: %s",seat.getSeatId(), slot));
                }
            }
        }
        return reservations;
    }

    public Collection<Seat> getAllSeats() {
        return seatMap.values();
    }

    public Collection<Student> getAllStudents() {
        return studentMap.values();
    }

    public void displayStatistics() {
        System. out.println (" \n = = = = = = = = = = System of statistical information = = = = = = = = = = ");
        System.out.println(" total number of seats: "+ seatMap.size());
        System.out.println(" total number of students: "+ studentMap.size());

        int totalReservations = 0;
        int occupiedSeats = 0;
        int awaySeats = 0;

        for (Seat seat : seatMap.values()) {
            totalReservations += seat.getReservations().size();
            String status = seat.getCurrentStatus();
            if ("occupied".equals(status)) {
                occupiedSeats++;
            } else if ("temporarily_away".equals(status)) {
                awaySeats++;
            }
        }

        System.out.println(" Total Reservations: "+ totalReservations);
        System.out.println(" Currently occupied seats: "+ occupiedSeats);
        System.out.println(" Current number of detached seats: "+ awaySeats);
        System.out.println(" current available seats: "+ (seatMap.size() - occupiedSeats - awaySeats));
        System.out.println("================================\n");
    }
}
