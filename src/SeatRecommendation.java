public class SeatRecommendation implements Comparable<SeatRecommendation> {
    private Seat seat;
    private int score;
    private String reason;

    public SeatRecommendation(Seat seat, int score, String reason) {
        this.seat = seat;
        this.score = score;
        this.reason = reason;
    }

    @Override
    public int compareTo(SeatRecommendation other) {
        int scoreComparison = Integer.compare(other.score, this.score);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        return this.seat.getSeatId().compareTo(other.seat.getSeatId());
    }

    public Seat getSeat() {
        return seat;
    }

    public int getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return String.format(" Seat: %s, score: %d, reason: %s",seat.getSeatId(), score, reason);
    }
}
