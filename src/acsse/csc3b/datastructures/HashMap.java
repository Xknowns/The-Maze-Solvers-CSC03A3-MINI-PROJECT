package acsse.csc3b.datastructures;

/**
 * Custom implementation of a HashMap data structure
 * using separate chaining for collision handling.
 * 
 * This class stores key-value pairs and provides
 * efficient insertion, retrieval, and removal operations.
 * 
 * The implementation dynamically resizes when the
 * load factor threshold is reached to maintain performance.
 * 
 * @param <K> the type of keys maintained by the map
 * @param <V> the type of mapped values
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class HashMap<K, V> {
	/**
	 * Default initial capacity of the hash table.
	 */
    private static final int INITIAL_CAPACITY = 16;
    
    /**
     * Array used to store hash table entries.
     */
    private Entry<K, V>[] table;
    
    /**
     * Tracks the number of key-value pairs stored in the map.
     */
    private int size;

    /**
     * Constructs an empty HashMap with the default initial capacity.
     */
    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new Entry[INITIAL_CAPACITY];
    }

    /**
     * Represents a single key-value pair stored in the hash table.
     * 
     * Each entry may point to another entry to support
     * separate chaining during hash collisions.
     * 
     * @param <K> the key type
     * @param <V> the value type
     */
    private static class Entry<K, V> {
    	/**
    	 * Stores the key associated with the entry.
    	 */
        K key;
        
        /**
         * Stores the value associated with the key.
         */
        V value;
        
        /**
         * Reference to the next entry in the chain.
         */
        Entry<K, V> next;
        
        /**
         * Stores the computed hash value of the key.
         */
        int hash;

        /**
         * Constructs a new hash table entry.
         * 
         * @param key the key to store
         * @param value the value associated with the key
         * @param hash the computed hash value
         * @param next reference to the next entry in the chain
         */
        Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    /**
     * Inserts or updates a key-value pair in the map.
     * 
     * If the key already exists, its value is updated.
     * Otherwise, a new entry is added.
     * 
     * @param key the key to insert
     * @param value the value associated with the key
     */
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key || key.equals(e.key))) {
                e.value = value;
                return;
            }
        }

        table[index] = new Entry<>(key, value, hash, table[index]);
        size++;

        if (size >= table.length * 0.75) {
            resize(2 * table.length);
        }
    }

    /**
     * Retrieves the value associated with the specified key.
     * 
     * @param key the key to search for
     * @return the associated value, or null if the key does not exist
     */
    public V get(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key || key.equals(e.key))) {
                return e.value;
            }
        }
        return null;
    }

    /**
     * Determines whether the specified key exists in the map.
     * 
     * @param key the key to check
     * @return true if the key exists, otherwise false
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Removes the key-value pair associated with the specified key.
     * 
     * @param key the key to remove
     * @return the removed value, or null if the key does not exist
     */
    public V remove(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Entry<K, V> prev = null;

        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key || key.equals(e.key))) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    table[index] = e.next;
                }
                size--;
                return e.value;
            }
            prev = e;
        }
        return null;
    }

    /**
     * Returns the number of key-value pairs stored in the map.
     * 
     * @return the size of the map
     */
    public int size() {
        return size;
    }

    /**
     * Determines whether the map contains any entries.
     * 
     * @return true if the map is empty, otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a collection containing all keys stored in the map.
     * 
     * @return a linked list containing all keys
     */
    public LinkedList<K> keySet() {
        LinkedList<K> keys = new LinkedList<>();
        for (Entry<K, V> e : table) {
            while (e != null) {
                keys.addLast(e.key);
                e = e.next;
            }
        }
        return keys;
    }

    /**
     * Computes the hash value for the specified key.
     * 
     * @param key the key to hash
     * @return the computed hash value
     */
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    /**
     * Computes the index position within the hash table
     * for the specified hash value.
     * 
     * @param h the hash value
     * @param length the length of the hash table
     * @return the computed table index
     */
    private int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * Resizes the hash table when the load factor threshold
     * is exceeded.
     * 
     * Existing entries are rehashed into a larger table.
     * 
     * @param newCapacity the new table capacity
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1 << 30) {
            return;
        }

        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    /**
     * Transfers all entries from the current table
     * into a newly allocated table during resizing.
     * 
     * @param newTable the new hash table
     */
    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K, V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
}