package acsse.csc3b.datastructures;

/**
 * Custom implementation of a minimum Priority Queue
 * using a binary heap data structure.
 * 
 * This priority queue always removes the element with
 * the smallest priority value first.
 * 
 * The structure is primarily used by graph pathfinding
 * algorithms such as:
 * 
 * - Dijkstra's Algorithm
 * - A* Algorithm
 * 
 * The binary heap implementation provides efficient:
 * 
 * - insertion operations
 * - removal operations
 * - priority retrieval
 * 
 * @param <T> the type of elements stored in the queue
 *            which must implement Comparable
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class PriorityQueue<T extends Comparable<T>> {
    private Object[] heap;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    /**
     * Constructs an empty priority queue with the
     * default initial capacity.
     */
    public PriorityQueue() {
        heap = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Inserts an element into the priority queue.
     * 
     * The element is automatically positioned
     * according to heap ordering rules.
     * 
     * @param item the element to insert
     */
    public void enqueue(T item) {
        ensureCapacity();
        heap[size] = item;
        siftUp(size);
        size++;
    }

    /**
     * Removes and returns the element with the
     * smallest priority value.
     * 
     * @return the minimum-priority element,
     *         or null if the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) return null;
        T min = (T) heap[0];
        heap[0] = heap[size - 1];
        size--;
        siftDown(0);
        return min;
    }
    
    /**
     * Returns the element with the smallest priority
     * without removing it from the queue.
     * 
     * @return the minimum-priority element,
     *         or null if the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        return isEmpty() ? null : (T) heap[0];
    }

    /**
     * Determines whether the priority queue is empty.
     * 
     * @return true if the queue contains no elements,
     *         otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements currently stored
     * in the priority queue.
     * 
     * @return the queue size
     */
    public int size() {
        return size;
    }

    /**
     * Restores heap order by moving an element upward
     * through the binary heap.
     * 
     * This operation is performed after insertion.
     * 
     * @param index the starting index of the element
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            T current = (T) heap[index];
            T parentVal = (T) heap[parent];
            if (current.compareTo(parentVal) >= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    /**
     * Restores heap order by moving an element downward
     * through the binary heap.
     * 
     * This operation is performed after removal.
     * 
     * @param index the starting index of the element
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                T leftVal = (T) heap[left];
                T smallestVal = (T) heap[smallest];
                if (leftVal.compareTo(smallestVal) < 0) smallest = left;
            }
            if (right < size) {
                T rightVal = (T) heap[right];
                T smallestVal = (T) heap[smallest];
                if (rightVal.compareTo(smallestVal) < 0) smallest = right;
            }
            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Swaps two elements within the heap array.
     * 
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Expands the heap capacity when the current
     * storage array becomes full.
     * 
     * The heap size is doubled to improve efficiency.
     */
    private void ensureCapacity() {
        if (size >= heap.length) {
            Object[] newHeap = new Object[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }
}