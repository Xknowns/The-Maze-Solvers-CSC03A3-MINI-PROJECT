package acsse.csc3b.datastructures;

/**
 * Custom generic LinkedList implementation used throughout
 * the project for dynamic data storage.
 * 
 * This data structure supports operations commonly required
 * for:
 * 
 * - Queue implementations
 * - Stack implementations
 * - Graph traversal
 * - Dynamic collections
 * 
 * The list stores elements using linked nodes and allows
 * efficient insertion and removal operations at both ends.
 * 
 * @param <T> the type of elements stored in the linked list
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class LinkedList<T> {
	/**
	 * Reference to the first node in the list.
	 */
    private Node<T> head;
    
    /**
     * Reference to the last node in the list.
     */
    private Node<T> tail;
    
    /**
     * Tracks the number of elements stored in the list.
     */
    private int size;

    /**
     * Represents a single node within the linked list.
     * 
     * Each node stores:
     * - the element data
     * - a reference to the next node
     * 
     * @param <T> the type of data stored in the node
     */
    private static class Node<T> {
    	/**
    	 * Stores the data contained in the node.
    	 */
        T data;
        
        /**
         * Reference to the next node in the list.
         */
        Node<T> next;

        /**
         * Constructs a new node containing the specified data.
         * 
         * @param data the data to store in the node
         */
        Node(T data) {
            this.data = data;
        }
    }

    /**
     * Adds a new element to the end of the linked list.
     * 
     * @param data the element to add
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Adds a new element to the beginning of the linked list.
     * 
     * @param data the element to add
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        if (tail == null) tail = newNode;
        size++;
    }

    /**
     * Removes and returns the first element in the linked list.
     * 
     * @return the removed element, or null if the list is empty
     */
    public T removeFirst() {
        if (head == null) return null;
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return data;
    }

    /**
     * Removes and returns the last element in the linked list.
     * 
     * @return the removed element, or null if the list is empty
     */
    public T removeLast() {
        if (tail == null) return null;
        if (head == tail) {
            T data = head.data;
            head = tail = null;
            size--;
            return data;
        }
        Node<T> current = head;
        while (current.next != tail) {
            current = current.next;
        }
        T data = tail.data;
        tail = current;
        tail.next = null;
        size--;
        return data;
    }

    /**
     * Returns the first element in the list without removing it.
     * 
     * @return the first element, or null if the list is empty
     */
    public T peekFirst() {
        return head != null ? head.data : null;
    }

    /**
     * Determines whether the linked list contains any elements.
     * 
     * @return true if the list is empty, otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements stored in the linked list.
     * 
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Determines whether the specified element exists in the list.
     * 
     * @param data the element to search for
     * @return true if the element exists, otherwise false
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Converts the linked list into an array representation.
     * 
     * The returned array always contains a null terminator
     * to support the null-terminated traversal pattern used
     * throughout the project.
     * 
     * @param array the destination array
     * @return an array containing all elements in the list
     */
    public T[] toArray(T[] array) {
        // ALWAYS ensure array is at least size + 1 so we can add a null terminator
        if (array.length < size + 1) {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) java.lang.reflect.Array.newInstance(
                array.getClass().getComponentType(), size + 1);
            array = newArray;
        }
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            array[i++] = current.data;
            current = current.next;
        }
        // Guaranteed null terminator - array is always at least size + 1
        array[i] = null;
        return array;
    }
}