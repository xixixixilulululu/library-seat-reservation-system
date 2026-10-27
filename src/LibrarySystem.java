import java.util.*;


public class LibrarySystem {
    private SeatManager seatManager;
    private Scanner scanner;
    private String currentUserId;

    public LibrarySystem() {
        this.seatManager = new SeatManager();
        this.scanner = new Scanner(System.in);
        this.currentUserId = null;


        initializeSampleData();
    }


    private void initializeSampleData() {

        Student s1 = new Student("S001", "Sample Student One");
        s1.setPreferences("window", "power_outlet", "quiet");
        seatManager.addStudent(s1);

        Student s2 = new Student("S002", "Sample Student Two");
        s2.setPreferences("power_outlet", "near_restroom");
        seatManager.addStudent(s2);

        Student s3 = new Student("S003", "Sample Student Three");
        s3.setPreferences("window", "quiet");
        seatManager.addStudent(s3);


        Seat seat1 = new Seat("A101", "East Area on the first floor - By the window");
        seat1.setAttribute("window", true);
        seat1.setAttribute("power_outlet", true);
        seat1.setAttribute("quiet", true);
        seatManager.addSeat(seat1);

        Seat seat2 = new Seat("A102", "East Area on the first floor - middle");
        seat2.setAttribute("power_outlet", true);
        seatManager.addSeat(seat2);

        Seat seat3 = new Seat("A103", "East Area on the first floor - near the door");
        seat3.setAttribute("power_outlet", true);
        seat3.setAttribute("near_door", true);
        seatManager.addSeat(seat3);

        Seat seat4 = new Seat("B101", "West Area on the first floor - By the window");
        seat4.setAttribute("window", true);
        seat4.setAttribute("quiet", true);
        seatManager.addSeat(seat4);

        Seat seat5 = new Seat("B102", "West Area on the first floor - middle");
        seat5.setAttribute("near_restroom", true);
        seatManager.addSeat(seat5);


        Seat seat6 = new Seat("C201", "Second floor - By the window");
        seat6.setAttribute("window", true);
        seat6.setAttribute("power_outlet", true);
        seat6.setAttribute("quiet", true);
        seatManager.addSeat(seat6);

        Seat seat7 = new Seat("C202", "Second floor - Standard seats");
        seat7.setAttribute("power_outlet", true);
        seatManager.addSeat(seat7);

        Seat seat8 = new Seat("C203", "Second floor - By the door");
        seat8.setAttribute("near_door", true);
        seatManager.addSeat(seat8);


        seatManager.reserveSeat("A101", 9, 12, "S002");
        seatManager.reserveSeat("A101", 14, 17, "S003");
        seatManager.reserveSeat("B101", 10, 13, "S001");
    }


    public void start() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          Library Seat Reservation System                             ║");
        System.out.println("║   An efficient seat management solution based on AVL trees           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");


        login();

        while (true) {
            displayMainMenu();
            int choice = getIntInput("Please select the function: ");

            if (!handleMenuChoice(choice)) {
                break;
            }
        }

        System.out.println("\nThank you for using this system. Goodbye!");
        scanner.close();
    }


    private void login() {
        System.out.println("\n========== User login ==========");
        System.out.print("Please enter the student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = seatManager.getStudent(studentId);
        if (student == null) {
            System.out.println("The student ID does not exist!");
            System.out.print("Do you want to create a new account? (y/n): ");
            String create = scanner.nextLine().trim();
            if (create.equalsIgnoreCase("y")) {
                createNewStudent(studentId);
            } else {
                System.out.println("Log in with the default user S001");
                currentUserId = "S001";
            }
        } else {
            currentUserId = studentId;
            System.out.println("welcome, " + student.getName() + "!");
        }
    }


    private void createNewStudent(String studentId) {
        System.out.print("Please enter your name： ");
        String name = scanner.nextLine().trim();
        Student student = new Student(studentId, name);

        System.out.println("\nSet seat preferences (separate multiple preferences with Spaces):");
        System.out.println("Optional: window(near the window), power_outlet(socket), quiet(quiet), near_restroom(near the restroom)");
        System.out.print("Please enter your preference ");
        String prefsInput = scanner.nextLine().trim();
        if (!prefsInput.isEmpty()) {
            student.setPreferences(prefsInput.split("\\s+"));
        }

        seatManager.addStudent(student);
        currentUserId = studentId;
        System.out.println("The account has been created successfully!");
    }


    private void displayMainMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           Main menu                    ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Query seat availability             ║");
        System.out.println("║ 2. Intelligent seat recommendation     ║");
        System.out.println("║ 3. Reserve a seat                      ║");
        System.out.println("║ 4. Cancel the reservation              ║");
        System.out.println("║ 5. My reservation list                 ║");
        System.out.println("║ 6. On-site seat occupation             ║");
        System.out.println("║ 7. Leave temporarily                   ║");
        System.out.println("║ 8. Return to the seat                  ║");
        System.out.println("║ 9. Release the seat                    ║");
        System.out.println("║ 10. Check the status of all seats      ║");
        System.out.println("║ 11. Manage personal preferences        ║");
        System.out.println("║ 12. System statistical information     ║");
        System.out.println("║ 13. Log out                            ║");
        System.out.println("║ 0. Exit the system                     ║");
        System.out.println("╚════════════════════════════════════════╝");
    }


    private boolean handleMenuChoice(int choice) {
        seatManager.checkAllTimeouts(); // Check the timeout before each operation

        switch (choice) {
            case 1:
                checkAvailability();
                break;
            case 2:
                intelligentRecommendation();
                break;
            case 3:
                reserveSeat();
                break;
            case 4:
                cancelReservation();
                break;
            case 5:
                viewMyReservations();
                break;
            case 6:
                occupySeat();
                break;
            case 7:
                temporarilyLeave();
                break;
            case 8:
                returnToSeat();
                break;
            case 9:
                releaseSeat();
                break;
            case 10:
                viewAllSeats();
                break;
            case 11:
                managePreferences();
                break;
            case 12:
                seatManager.displayStatistics();
                break;
            case 13:
                System.out.println("\nLogging out...");
                login();
                break;
            case 0:
                return false;
            default:
                System.out.println("Invalid selection. Please try again!");
        }
        return true;
    }

//1///////////////////////////////////////////////////////////////////////////////////////////////
    private void checkAvailability() {
        System.out.println("\n========== Query available seats ==========");
        System.out.print("Please enter your seat ID (for example: A101): ");
        String seatId = scanner.nextLine().trim();

        int[] timeRange = getTimeRange();
        int startTime = timeRange[0];
        int endTime = timeRange[1];

        long startMs = System.currentTimeMillis();
        boolean available = seatManager.checkAvailability(seatId, startTime, endTime);
        long endMs = System.currentTimeMillis();

        System.out.println("\nQuery result:");
        if (available) {
            System.out.println("The seat " + seatId + " during " + startTime + ":00 - " +
                             endTime + ":00 is available!");
        } else {
            System.out.println("The seat " + seatId + " during " + startTime + ":00 - " +
                             endTime + ":00 has been reserved!");


            Seat seat = seatManager.getSeat(seatId);
            if (seat != null) {
                System.out.println("\nThe reservation status of this seat:");
                for (TimeSlot slot : seat.getReservations().getAllTimeSlots()) {
                    System.out.println("  " + slot);
                }
            }
        }
        System.out.println("\nThe following sentences are only used in cps2232 presentation and project");
        System.out.println("Query time-consuming: " + (endMs - startMs) + "milliseconds (AVL tree O(log n) complexity)");
    }

    //2///////////////////////////////////////////////////////////////////////////////////////////////
    private void intelligentRecommendation() {
        System.out.println("\n========== Intelligent seat recommendation ==========");
        int[] timeRange = getTimeRange();
        int startTime = timeRange[0];
        int endTime = timeRange[1];
        int topN = getPositiveIntInput("How many seats do you need to recommend (3 to 5 are suggested)? ");

        System.out.println("\nThe following sentences are only used in cps2232 presentation and project");
        System.out.println("Analyzing...");
        System.out.println("Traverse the AVL tree of all seats");
        System.out.println("Match based on your personal preferences");
        System.out.println("Calculate a weighted score from your seat preferences");
        System.out.println("Sort available seats by score\n");

        long startMs = System.currentTimeMillis();
        List<SeatRecommendation> recommendations = seatManager.recommendSeats(
            startTime, endTime, currentUserId, topN);
        long endMs = System.currentTimeMillis();

        if (recommendations.isEmpty()) {
            System.out.println("Sorry, there are no available seats for this time slot");
        } else {
            System.out.println("We recommend the following seats for you (sorted by matching degree):\n");
            int rank = 1;
            for (SeatRecommendation rec : recommendations) {
                Seat seat = rec.getSeat();
                System.out.println("Recommended " + rank + ": seat " + seat.getSeatId());
                System.out.println("  Location: " + seat.getLocation());
                System.out.println("  Recommended score: " + rec.getScore() + " points");
                System.out.println("  Recommendation reasons: " + rec.getReason());


                System.out.print("  Seat characteristics ");
                List<String> features = new ArrayList<>();
                if (seat.hasAttribute("window")) features.add("By the window");
                if (seat.hasAttribute("power_outlet")) features.add("with a power socket");
                if (seat.hasAttribute("quiet")) features.add("quiet");
                if (seat.hasAttribute("near_restroom")) features.add("close to the bathroom");
                if (seat.hasAttribute("near_door")) features.add("near the door");
                System.out.println(features.isEmpty() ? "Standard seat" : String.join(", ", features));
                System.out.println();
                rank++;
            }
        }
        System.out.println("\nThe following sentences are only used in cps2232 presentation and project");
        System.out.println("Recommendation time: " + (endMs - startMs) + " milliseconds");
    }

    //3///////////////////////////////////////////////////////////////////////////////////////////////
    private void reserveSeat() {
        System.out.println("\n========== Reserve a seat ==========");
        System.out.print("Please enter your seat ID： ");
        String seatId = scanner.nextLine().trim();

        int[] timeRange = getTimeRange();
        int startTime = timeRange[0];
        int endTime = timeRange[1];

        boolean success = seatManager.reserveSeat(seatId, startTime, endTime, currentUserId);

        if (success) {
            System.out.println(" Reservation successful! Seat " + seatId + " Reserved for you " +
                             startTime + ":00 - " + endTime + ":00 ");
        } else {
            System.out.println("Reservation failed! This time slot has been occupied or there are no seats available");
        }
    }

    //4///////////////////////////////////////////////////////////////////////////////////////////////
    private void cancelReservation() {
        System.out.println("\n========== Cancel the reservation ==========");

        // Display the user's reservation first
        List<String> reservations = seatManager.getStudentReservations(currentUserId);
        if (reservations.isEmpty()) {
            System.out.println("You currently have no appointments!");
            return;
        }

        System.out.println("Your reservation list:");
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println((i + 1) + ". " + reservations.get(i));
        }

        System.out.print("\nPlease enter your seat ID： ");
        String seatId = scanner.nextLine().trim();

        int[] timeRange = getTimeRange();
        int startTime = timeRange[0];
        int endTime = timeRange[1];

        boolean success = seatManager.cancelReservation(seatId, startTime, endTime, currentUserId);

        if (success) {
            System.out.println("The reservation has been cancelled.!");
        } else {
            System.out.println("Cancellation failed! The reservation record was not found");
        }
    }

    //5///////////////////////////////////////////////////////////////////////////////////////////////
    private void viewMyReservations() {
        System.out.println("\n========== My reservation list ==========");
        List<String> reservations = seatManager.getStudentReservations(currentUserId);

        if (reservations.isEmpty()) {
            System.out.println("You currently have no appointments!");
        } else {
            for (int i = 0; i < reservations.size(); i++) {
                System.out.println((i + 1) + ". " + reservations.get(i));
            }
        }
    }

    //6///////////////////////////////////////////////////////////////////////////////////////////////
    private void occupySeat() {
        System.out.println("\n========== On-site seat occupation ==========");
        System.out.print("Please enter your seat ID: ");
        String seatId = scanner.nextLine().trim();

        boolean success = seatManager.occupySeat(seatId, currentUserId);

        if (success) {
            System.out.println(" Seat " + seatId + " Occupied! Wish you a pleasant study!");
        } else {
            System.out.println(" Occupation failed! The seat does not exist or has been occupied");
        }
    }

    //7///////////////////////////////////////////////////////////////////////////////////////////////
    private void temporarilyLeave() {
        System.out.println("\n========== Leave temporarily ==========");
        System.out.print("Please enter your seat ID: ");
        String seatId = scanner.nextLine().trim();

        int duration = getPositiveIntInput("Estimated departure duration (minutes): ");

        boolean success = seatManager.temporarilyLeaveSeat(seatId, currentUserId, duration);

        if (success) {
            System.out.println(" Your temporary absence status has been recorded!");
            System.out.println("  The seat will be reserved for you " + duration + " minutes");
            System.out.println("  The seat will be automatically released after the time limit is exceeded");
            System.out.println("\n Stack operation: The temporarily removed state has been pushed onto the state stack");
        } else {
            System.out.println("Operation failed! Please confirm that you have occupied this seat");
        }
    }

    //8///////////////////////////////////////////////////////////////////////////////////////////////
    private void returnToSeat() {
        System.out.println("\n========== Return to the seat ==========");
        System.out.print("Please enter your seat ID: ");
        String seatId = scanner.nextLine().trim();

        boolean success = seatManager.returnToSeat(seatId, currentUserId);

        if (success) {
            System.out.println(" Welcome back! The seat status has been restored");
            System.out.println("\n Stack operation: The transient state has been popped from the state stack");
        } else {
            System.out.println(" Operation failed! Please confirm the seat status");
        }
    }

    //9///////////////////////////////////////////////////////////////////////////////////////////////
    private void releaseSeat() {
        System.out.println("\n========== Release the seat ==========");
        System.out.print("Please enter your seat ID: ");
        String seatId = scanner.nextLine().trim();

        boolean success = seatManager.releaseSeat(seatId, currentUserId);

        if (success) {
            System.out.println(" The seats have been released! Thank you for using it!");
        } else {
            System.out.println(" Release failed! Please confirm that you have occupied this seat");
        }
    }

    //10///////////////////////////////////////////////////////////////////////////////////////////////
    private void viewAllSeats() {
        System.out.println("\n========== All seat status ==========");

        Collection<Seat> seats = seatManager.getAllSeats();
        for (Seat seat : seats) {
            System.out.println("\n" + seat.getSeatId() + " - " + seat.getLocation());
            System.out.println("  Current status: " + seat.getCurrentStatus());

            if (seat.getCurrentStudentId() != null) {
                Student student = seatManager.getStudent(seat.getCurrentStudentId());
                System.out.println("  User: " + student.getName());
            }

            List<TimeSlot> slots = seat.getReservations().getAllTimeSlots();
            if (!slots.isEmpty()) {
                System.out.println("  Reservation status:");
                for (TimeSlot slot : slots) {
                    System.out.println("    " + slot);
                }
            }
        }
    }

    //11///////////////////////////////////////////////////////////////////////////////////////////////
    private void managePreferences() {
        System.out.println("\n========== Manage personal preferences ==========");
        Student student = seatManager.getStudent(currentUserId);

        System.out.println("Current preference: " + student.getPreferences());
        System.out.println("\n1. Add preference");
        System.out.println("2.  delete preference");
        System.out.println("3. reset preference");

        int choice = getIntInput("Please select: ");

        switch (choice) {
            case 1:
                System.out.println("Optional: window(near the window), power_outlet(socket), quiet(quiet), near_restroom(near the restroom)");
                System.out.print("Please enter the preferences you want to add: ");
                String addPref = scanner.nextLine().trim();
                student.addPreference(addPref);
                System.out.println(" Preferences have been added.!");
                break;
            case 2:
                System.out.print("Please enter the preference you want to delete: ");
                String removePref = scanner.nextLine().trim();
                student.removePreference(removePref);
                System.out.println(" The preference has been deleted!");
                break;
            case 3:
                System.out.print("Please enter a new preference (separated by Spaces): ");
                String newPrefs = scanner.nextLine().trim();
                if (!newPrefs.isEmpty()) {
                    student.setPreferences(newPrefs.split("\\s+"));
                    System.out.println(" Preferences have been reset.!");
                }
                break;
            default:
                System.out.println("Invalid choice!");
        }

        System.out.println("New preference Settings: " + student.getPreferences());
    }

    //Integer///////////////////////////////////////////////////////////////////////////////////////////////
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid numbers!");
            }
        }
    }

    private int getPositiveIntInput(String prompt) {
        while (true) {
            int value = getIntInput(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a number greater than zero!");
        }
    }

    private int[] getTimeRange() {
        while (true) {
            int startTime = getIntInput("Please enter the start time (0-23): ");
            int endTime = getIntInput("Please enter the end time (1-24): ");
            if (startTime >= 0 && startTime < endTime && endTime <= 24) {
                return new int[] {startTime, endTime};
            }
            System.out.println("Please enter a valid range with 0 <= start < end <= 24.");
        }
    }


    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }
}
