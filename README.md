# Library Seat Reservation System

An educational Java command-line prototype that demonstrates how an AVL tree, a stack, hash maps, and weighted preference scoring can support library seat reservations.

The application lets a user check availability, reserve or cancel a time slot, receive preference-based seat recommendations, occupy a seat, leave temporarily, return, and release the seat. It stores all data in memory and is intended for data-structures coursework and portfolio review.

## Highlights

- **AVL-tree reservation index:** each seat stores non-overlapping reservation intervals in a self-balancing search tree.
- **Conflict detection:** availability checks follow the tree ordering and run in `O(log n)` under the non-overlapping-interval invariant.
- **Preference-weighted recommendations:** available seats receive scores for matching attributes such as a window, power outlet, or quiet area, then are sorted by score.
- **Stack-based seat state:** occupied and temporarily-away states are pushed and popped in last-in, first-out order.
- **Hash-map lookup:** seats and students are retrieved by ID.
- **No external dependencies:** the project builds with the Java Development Kit alone.

## Repository Structure

```text
.
├── src/                    Java application source
├── tests/                  Dependency-free smoke test
├── docs/                   Public project report and notes
├── .gitignore
└── README.md
```

## Requirements

- JDK 17 or later

The code also compiles on newer JDK releases.

## Build and Run

From the repository root:

```bash
mkdir -p build/classes
javac -d build/classes src/*.java
java -cp build/classes LibrarySystem
```

Use one of the sample accounts:

| Student ID | Sample profile |
|---|---|
| `S001` | Window, power outlet, and quiet-area preferences |
| `S002` | Power-outlet and restroom-proximity preferences |
| `S003` | Window and quiet-area preferences |

## Run the Tests

Compile the source and smoke test together:

```bash
mkdir -p build/test-classes
javac -d build/test-classes src/*.java tests/SystemSmokeTest.java
java -ea -cp build/test-classes SystemSmokeTest
```

Run the optional microbenchmarks:

```bash
mkdir -p build/classes
javac -d build/classes src/*.java
java -cp build/classes PerformanceTest
```

Benchmark results vary by computer, Java runtime, warm-up state, and background load. They are included for demonstration rather than as production performance guarantees.

## Design Overview

```text
LibrarySystem
    |
    v
SeatManager ---- HashMap<String, Student>
    |
    +---- HashMap<String, Seat>
                       |
                       +---- AVLTree<TimeSlot>
                       |
                       +---- Stack<SeatStatus>
```

### Reservation Model

Time is represented as whole-hour values from `0` through `24`. A reservation is valid when `0 <= start < end <= 24`. Adjacent reservations such as `09:00-10:00` and `10:00-11:00` do not conflict.

### Recommendation Model

For every available seat, the system adds weights for attributes that match the student's preferences. It also applies a small penalty to a near-door seat when that feature was not requested and a small bonus to seats with fewer than three reservations. The results are sorted by descending score, with the seat ID used as a deterministic tie-breaker.

This is a weighted heuristic, not machine learning or dynamic programming.

## Scope and Limitations

This repository is a local teaching prototype. It does **not** include:

- persistent storage or a database;
- authentication or authorization;
- concurrency control for simultaneous users;
- a network API, browser interface, or mobile application;
- real-time notifications or production monitoring.

All data is lost when the program exits. A production implementation would require persistence, validation at service boundaries, concurrency-safe transactions, security controls, and automated integration testing.

## Documentation

See [`docs/public-project-report.pdf`](docs/public-project-report.pdf) for a concise, anonymized report describing the design, algorithms, verification, and limitations.

## Privacy

The public version uses fictional sample accounts. Student numbers, personal names, instructor details, local IDE settings, and operating-system metadata from the coursework submission are intentionally excluded.

## License

No license has been granted for reuse or redistribution. All rights are reserved by the project authors.
