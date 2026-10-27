import java.util.List;

public class SystemSmokeTest {
    public static void main(String[] args) {
        SeatManager manager = new SeatManager();

        Student student = new Student("S001", "Sample Student");
        student.setPreferences("window", "power_outlet");
        manager.addStudent(student);

        Seat preferredSeat = new Seat("A101", "First floor window area");
        preferredSeat.setAttribute("window", true);
        preferredSeat.setAttribute("power_outlet", true);
        manager.addSeat(preferredSeat);

        Seat standardSeat = new Seat("A102", "First floor center area");
        manager.addSeat(standardSeat);

        check(manager.reserveSeat("A101", 9, 11, "S001"), "first reservation should succeed");
        check(!manager.reserveSeat("A101", 10, 12, "S001"), "overlapping reservation should fail");
        check(manager.checkAvailability("A101", 12, 14), "non-overlapping interval should be available");
        check(manager.cancelReservation("A101", 9, 11, "S001"), "reservation cancellation should succeed");

        List<SeatRecommendation> recommendations = manager.recommendSeats(9, 11, "S001", 2);
        check(!recommendations.isEmpty(), "recommendation list should not be empty");
        check("A101".equals(recommendations.get(0).getSeat().getSeatId()),
                "preference-matching seat should rank first");

        check(manager.occupySeat("A101", "S001"), "available seat should be occupiable");
        check(manager.temporarilyLeaveSeat("A101", "S001", 10), "temporary leave should succeed");
        check(manager.returnToSeat("A101", "S001"), "return from temporary leave should succeed");
        check(manager.releaseSeat("A101", "S001"), "occupied seat should be releasable");
        check("available".equals(preferredSeat.getCurrentStatus()), "released seat should be available");

        check(manager.occupySeat("A101", "S001"), "seat should be occupiable again");
        check(manager.temporarilyLeaveSeat("A101", "S001", 10), "second temporary leave should succeed");
        check(manager.releaseSeat("A101", "S001"), "seat can be released while temporarily away");
        check("available".equals(preferredSeat.getCurrentStatus()),
                "release while away should restore available status");

        check(!manager.temporarilyLeaveSeat("A101", "S001", 0),
                "non-positive temporary-leave duration should fail");
        check(manager.recommendSeats(9, 11, "S001", 0).isEmpty(),
                "non-positive recommendation count should return an empty list");

        boolean invalidRangeRejected = false;
        try {
            new TimeSlot(12, 12, "S001");
        } catch (IllegalArgumentException expected) {
            invalidRangeRejected = true;
        }
        check(invalidRangeRejected, "invalid time range should be rejected");

        System.out.println("All smoke tests passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
