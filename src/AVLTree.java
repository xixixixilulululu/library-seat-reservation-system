import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private AVLNode root;

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public boolean insert(TimeSlot timeSlot) {

        if (hasConflict(timeSlot.getStartTime(), timeSlot.getEndTime())) {
            return false;
        }
        root = insertNode(root, timeSlot);
        return true;
    }

    private AVLNode insertNode(AVLNode node, TimeSlot timeSlot) {

        if (node == null) {
            return new AVLNode(timeSlot);
        }

        int cmp = timeSlot.compareTo(node.timeSlot);
        if (cmp < 0) {
            node.left = insertNode(node.left, timeSlot);
        } else if (cmp > 0) {
            node.right = insertNode(node.right, timeSlot);
        } else {
            return node;
        }
        updateHeight(node);
        int balance = getBalance(node);
        // Left Left Case
        if (balance > 1 && timeSlot.compareTo(node.left.timeSlot) < 0) {
            return rotateRight(node);
        }
        // Right Right Case
        if (balance < -1 && timeSlot.compareTo(node.right.timeSlot) > 0) {
            return rotateLeft(node);
        }
        // Left Right Case
        if (balance > 1 && timeSlot.compareTo(node.left.timeSlot) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Right Left Case
        if (balance < -1 && timeSlot.compareTo(node.right.timeSlot) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    //Core function: Check whether the specified time period conflicts with existing reservations
    //Time complexity :O(log n)


    public boolean hasConflict(int startTime, int endTime) {
        return hasConflictHelper(root, startTime, endTime);
    }

    private boolean hasConflictHelper(AVLNode node, int startTime, int endTime) {
        if (node == null) {
            return false;
        }
        if (node.timeSlot.overlaps(startTime, endTime)) {
            return true;
        }
        if (endTime <= node.timeSlot.getStartTime()) {
            return hasConflictHelper(node.left, startTime, endTime);
        }
        if (startTime >= node.timeSlot.getEndTime()) {
            return hasConflictHelper(node.right, startTime, endTime);
        }
        return hasConflictHelper(node.left, startTime, endTime) ||
               hasConflictHelper(node.right, startTime, endTime);
    }


    public boolean delete(int startTime, int endTime, String studentId) {
        if (root == null) {
            return false;
        }
        int sizeBefore = size();
        root = deleteNode(root, startTime, endTime, studentId);
        return size() < sizeBefore;
    }

    private AVLNode deleteNode(AVLNode node, int startTime, int endTime, String studentId) {
        if (node == null) {
            return null;
        }

        if (endTime <= node.timeSlot.getStartTime()) {
            node.left = deleteNode(node.left, startTime, endTime, studentId);
        } else if (startTime >= node.timeSlot.getEndTime()) {
            node.right = deleteNode(node.right, startTime, endTime, studentId);
        } else if (node.timeSlot.getStartTime() == startTime &&
                   node.timeSlot.getEndTime() == endTime &&
                   node.timeSlot.getStudentId().equals(studentId)) {

            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            AVLNode minNode = findMin(node.right);
            node.timeSlot = minNode.timeSlot;
            node.right = deleteNode(node.right, minNode.timeSlot.getStartTime(),
                                   minNode.timeSlot.getEndTime(), minNode.timeSlot.getStudentId());
        } else {

            node.left = deleteNode(node.left, startTime, endTime, studentId);
            node.right = deleteNode(node.right, startTime, endTime, studentId);
        }

        if (node == null) {
            return null;
        }

        updateHeight(node);

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode findMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(AVLNode node, List<TimeSlot> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.timeSlot);
            inorderTraversal(node.right, result);
        }
    }

    public int size() {
        return sizeHelper(root);
    }

    private int sizeHelper(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeHelper(node.left) + sizeHelper(node.right);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
