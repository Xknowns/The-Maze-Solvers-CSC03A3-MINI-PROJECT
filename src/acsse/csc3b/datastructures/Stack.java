package acsse.csc3b.datastructures;

/**
 * Custom implementation of a Stack data structure
 * using the LinkedList class.
 * 
 * This stack follows the LIFO (Last In, First Out)
 * principle where the most recently added element
 * is removed first.
 * 
 * The stack is used throughout the project for:
 * 
 * - Graph traversal
 * - Depth-first search operations
 * - Backtracking
 * - Temporary data storage
 * 
 * @param <T> the type of elements stored in the stack
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class Stack<T> {
    private LinkedList<T> list = new LinkedList<>();

    /**
     * Pushes an element onto the top of the stack.
     * 
     * @param item the element to add
     */
    public void push(T item) {
        list.addFirst(item);
    }

    /**
     * Removes and returns the element at the top of the stack.
     * 
     * @return the removed element,
     *         or null if the stack is empty
     */
    public T pop() {
        return list.removeFirst();
    }

    /**
     * Returns the element at the top of the stack
     * without removing it.
     * 
     * @return the top element,
     *         or null if the stack is empty
     */
    public T peek() {
        return list.peekFirst();
    }

    /**
     * Determines whether the stack contains any elements.
     * 
     * @return true if the stack is empty,
     *         otherwise false
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements currently stored in the stack.
     * 
     * @return the stack size
     */
    public int size() {
        return list.size();
    }
}