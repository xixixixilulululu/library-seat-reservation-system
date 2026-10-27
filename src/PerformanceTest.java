import java.util.*;


public class PerformanceTest {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Library Seat Reservation System - Illustrative Benchmarks");
        System.out.println("============================================================");

        // Test 1: Performance of AVL tree conflict detection
        testAVLTreePerformance();

        //Test 2: Intelligent Recommendation Performance
        testRecommendationPerformance();

        // Test 3: Stack Operation Performance
        testStackPerformance();

        //Test 4: Comparative Test (AVL Tree vs. Array)
        compareWithArrayApproach();
    }

//1///////////////////////////////////////////////////////////////////////////////////////////////////
    private static void testAVLTreePerformance() {
        System.out.println("========== Test 1: Performance of AVL tree conflict detection ==========\n");

        Seat seat = new Seat("TEST001", "Test seat");
        Random random = new Random(42);

        int[] testSizes = {4, 8, 16, 24};

        for (int size : testSizes) {
            seat = new Seat("TEST001", "Test seat");

            for (int i = 0; i < size; i++) {
                seat.reserve(i, i + 1, "S" + i);
            }

            int queryCount = 100_000;
            long startTime = System.nanoTime();
            for (int i = 0; i < queryCount; i++) {
                int queryStart = random.nextInt(24);
                int queryEnd = Math.min(24, queryStart + 1);
                seat.isAvailable(queryStart, queryEnd);
            }
            long endTime = System.nanoTime();

            double totalTimeMs = (endTime - startTime) / 1_000_000.0;
            System.out.printf("Stored reservations: %2d | %,d queries: %.3f ms | average: %.6f ms%n",
                            seat.getReservations().size(), queryCount, totalTimeMs, totalTimeMs / queryCount);
        }

        System.out.println("\nResults are illustrative and depend on the Java runtime and computer.");
        System.out.println("The AVL tree keeps conflict checks logarithmic in the number of stored intervals.\n");
    }

    //2///////////////////////////////////////////////////////////////////////////////////////////////////
    private static void testRecommendationPerformance() {
        System.out.println("\n========== Test 2: Intelligent Recommendation Performance ==========\n");

        SeatManager manager = new SeatManager();

        Student student = new Student("S001", "Test students");
        student.setPreferences("window", "power_outlet", "quiet");
        manager.addStudent(student);

        int[] seatCounts = {10, 50, 100, 200};

        for (int count : seatCounts) {
            manager = new SeatManager();
            manager.addStudent(student);

            for (int i = 0; i < count; i++) {
                Seat seat = new Seat("SEAT" + i, "Seat" + i);
                seat.setAttribute("window", i % 3 == 0);
                seat.setAttribute("power_outlet", i % 2 == 0);
                seat.setAttribute("quiet", i % 4 == 0);

                // Add some random reservations for each seat
                Random random = new Random(42 + i);
                for (int j = 0; j < 5; j++) {
                    int start = random.nextInt(18);
                    int end = start + 1 + random.nextInt(4);
                    seat.reserve(start, end, "S" + j);
                }

                manager.addSeat(seat);
            }
            //test
            long startTime = System.nanoTime();
            List<SeatRecommendation> recommendations =
                manager.recommendSeats(8, 11, "S001", 5);
            long endTime = System.nanoTime();

            double time = (endTime - startTime) / 1000000.0; //Nanosecond to millisecond conversion
            System.out.printf("Number of seats: %3d; Recommendation time consumption: %.3f; ms; number of recommendation results: %d\n",
                            count, time, recommendations.size());
        }

        System.out.println("\nResults are illustrative and depend on the Java runtime and computer.");
        System.out.println("Recommendation combines seat traversal, conflict checks, weighted scoring, and sorting.\n");
    }

    //3///////////////////////////////////////////////////////////////////////////////////////////////////
    private static void testStackPerformance() {
        System.out.println("\n========== Test 3: Stack Operation Performance ==========\n");

        Seat seat = new Seat("STACK_TEST", "Stack test seat");

        // test
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            seat.occupy("S001");
            seat.temporarilyLeave("S001", 15);
            seat.returnToSeat("S001");
            seat.release();
        }
        long endTime = System.nanoTime();

        double totalTime = (endTime - startTime) / 1000000.0;
        double avgTime = totalTime / 100000;

        System.out.printf("Total time consumption of 100,000 stack operations (push stack + pop stack) : %.2f ms\n", totalTime);
        System.out.printf("Average time consumption per operation: %.6f ms\n", avgTime);

        System.out.println("\nStack push and pop operations are O(1).");
        System.out.println("Measured timing is environment-dependent and is included only as an illustration.\n");
    }

    //4///////////////////////////////////////////////////////////////////////////////////////////////////
    private static void compareWithArrayApproach() {
        System.out.println("\n========== Test 4: Comparison of AVL Tree vs. Array Schemes ==========\n");

        Random random = new Random(42);
        int testSize = 24;
        int queryCount = 100_000;

        // test
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            timeSlots.add(new TimeSlot(i, i + 1, "S" + i));
        }

        // plan1: AVL tree/////////////////////
        System.out.println("Option 1: AVL tree");
        AVLTree avlTree = new AVLTree();

        long avlInsertStart = System.nanoTime();
        for (TimeSlot slot : timeSlots) {
            avlTree.insert(slot);
        }
        long avlInsertEnd = System.nanoTime();

        long avlQueryStart = System.nanoTime();
        for (int i = 0; i < queryCount; i++) {
            int start = random.nextInt(24);
            int end = Math.min(24, start + 1);
            avlTree.hasConflict(start, end);
        }
        long avlQueryEnd = System.nanoTime();

        double avlInsertTime = (avlInsertEnd - avlInsertStart) / 1000000.0;
        double avlQueryTime = (avlQueryEnd - avlQueryStart) / 1000000.0;

        System.out.printf("  Insert %d appointments: %.3f ms\n", testSize, avlInsertTime);
        System.out.printf("  %,d queries: %.3f ms\n", queryCount, avlQueryTime);

        // plan 2: Array (Simulation)//////////////
        System.out.println("\nSolution 2: Array (Linear Query)");
        List<TimeSlot> arrayList = new ArrayList<>();

        long arrayInsertStart = System.nanoTime();
        arrayList.addAll(timeSlots);
        long arrayInsertEnd = System.nanoTime();

        long arrayQueryStart = System.nanoTime();
        for (int i = 0; i < queryCount; i++) {
            int start = random.nextInt(24);
            int end = Math.min(24, start + 1);
            // Simulate linear queries
            hasConflictLinear(arrayList, start, end);
        }
        long arrayQueryEnd = System.nanoTime();

        double arrayInsertTime = (arrayInsertEnd - arrayInsertStart) / 1000000.0;
        double arrayQueryTime = (arrayQueryEnd - arrayQueryStart) / 1000000.0;

        System.out.printf("  Insert %d appointments: %.3f ms\n", testSize, arrayInsertTime);
        System.out.printf("  %,d queries: %.3f ms\n", queryCount, arrayQueryTime);

        //comparison
        System.out.println("\n Performance comparison:");
        System.out.printf("  Query-time ratio (linear / AVL): %.1f%n", arrayQueryTime / avlQueryTime);

        System.out.println("\nThis small benchmark compares the same non-overlapping intervals in both structures.");
        System.out.println("Treat the measured ratio as environment-dependent, not as a universal guarantee.\n");
    }

     //Linear conflict detection of simulated arrays
    private static boolean hasConflictLinear(List<TimeSlot> slots, int start, int end) {
        for (TimeSlot slot : slots) {
            if (slot.overlaps(start, end)) {
                return true;
            }
        }
        return false;
    }
}
