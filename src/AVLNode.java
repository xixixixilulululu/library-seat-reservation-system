
public class AVLNode {
    TimeSlot timeSlot;
    AVLNode left;
    AVLNode right;
    int height;

    public AVLNode(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.height = 1;
    }

    @Override
    public String toString() {
        return timeSlot.toString();
    }
}
