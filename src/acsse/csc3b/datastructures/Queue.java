package acsse.csc3b.datastructures;

/**
 * Custom implementation of a Queue data structure
 * using the LinkedList class.
 * 
 * This queue follows the FIFO (First In, First Out)
 * principle where elements are removed in the same
 * order they were inserted.
 * 
 * The queue is used throughout the project for:
 * 
 * - Graph traversal
 * - Breadth-first search operations
 * - Pathfinding support
 * - Temporary data storage
 * 
 * @param <T> the type of elements stored in the queue
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class Queue<T> {
    private LinkedList<T> list = new LinkedList<>();

    /**
     * Inserts an element at the rear of the queue.
     * 
     * @param item the element to add
     */
    public void enqueue(T item) {
        list.addLast(item);
    }

    /**
     * Removes and returns the element at the front
     * of the queue.
     * 
     * @return the removed element,
     *         or null if the queue is empty
     */
    public T dequeue() {
        return list.removeFirst();
    }

    /**
     * Returns the element at the front of the queue
     * without removing it.
     * 
     * @return the front element,
     *         or null if the queue is empty
     */
    public T peek() {
        return list.peekFirst();
    }

    /**
     * Determines whether the queue contains any elements.
     * 
     * @return true if the queue is empty,
     *         otherwise false
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements stored in the queue.
     * 
     * @return the queue size
     */
    public int size() {
        return list.size();
    }
}