package acsse.csc3b.datastructures;

/**
 * Custom implementation of a Set data structure
 * using the HashMap class internally.
 * 
 * A set stores unique elements only, meaning
 * duplicate values are not allowed.
 * 
 * This implementation is used throughout the project for:
 * 
 * - Tracking visited graph nodes
 * - Preventing duplicate entries
 * - Managing unique collections
 * - Supporting graph traversal operations
 * 
 * @param <T> the type of elements stored in the set
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class Set<T> {
    private HashMap<T, Object> map;
    private static final Object PRESENT = new Object();

    /**
     * Constructs an empty Set.
     */
    public Set() {
        map = new HashMap<>();
    }

    /**
     * Adds an element to the set if it does not already exist.
     * 
     * @param item the element to add
     * @return true if the element was added successfully,
     *         otherwise false if it already exists
     */
    public boolean add(T item) {
        if (map.containsKey(item)) return false;
        map.put(item, PRESENT);
        return true;
    }

    /**
     * Determines whether the specified element exists in the set.
     * 
     * @param item the element to search for
     * @return true if the element exists,
     *         otherwise false
     */
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Removes the specified element from the set.
     * 
     * @param item the element to remove
     * @return true if the element was removed successfully,
     *         otherwise false
     */
    public boolean remove(T item) {
        return map.remove(item) != null;
    }

    /**
     * Returns the number of unique elements stored in the set.
     * 
     * @return the set size
     */
    public int size() {
        return map.size();
    }

    /**
     * Determines whether the set contains any elements.
     * 
     * @return true if the set is empty,
     *         otherwise false
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Converts the set into a linked list containing
     * all stored elements.
     * 
     * @return a linked list containing all set elements
     */
    public LinkedList<T> toList() {
        return map.keySet();
    }
}