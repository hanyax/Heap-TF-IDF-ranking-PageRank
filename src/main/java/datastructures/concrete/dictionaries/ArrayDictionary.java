package datastructures.concrete.dictionaries;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    public static final int DEFAULT_CAPACITY  = 4;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        size = 0;
        pairs = this.makeArrayOfPairs(DEFAULT_CAPACITY);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }
    
    /**
     * @return  the value corresponding to the given key
     * @throws  NoSuchKeyException if the dictionary does not contain the given key
     */
    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if ((key == null && pairs[i].key == null) || 
            		(pairs[i].key != null && pairs[i].key.equals(key))) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException("Key does not exist");
    }
    
    /**
     * @modifies    this
     * @effect      Adds the key-value pair to the dictionary. If the key 
     *              already exists in the dictionary, replace its value with the given one.
     */
    @Override
    public void put(K key, V value) {
    		int i = this.containsKeyHelper(key);
        if (i != -1) {
            pairs[i].value = value; 
        } else {
            size++;           
            ensureCapacity();
            pairs[size - 1] = new Pair<K, V>(key, value);
        }
    }
    
    /**
     * Make sure the array has enough capacity
     */
    private void ensureCapacity() {
       if (pairs.length <= size) {
           int newCapacity = pairs.length * 2;
           pairs = Arrays.copyOf(pairs, newCapacity);
       }
    }

    /**
     * @modifies    this
     * @effect      Remove the key-value pair corresponding to the given key from the dictionary.
     * @throws      NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V remove(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException("Can not remove an non-existed key");
        }
        int i = 0;
        if (key == null) {
            while (pairs[i].key!= null) {
                i++;
            }
        } else {
            while (!pairs[i].key.equals(key)) {
                i++;
            }
        }
        V removed = pairs[i].value;
        for (int j = i; j < size - 1; j++) {
            pairs[j] = pairs[j + 1];
        }
        size--;
        return removed;
    }
    
    /**
     * @return true if key is in the dics, otherwise false
     */
    @Override
    public boolean containsKey(K key) {
    		return containsKeyHelper(key) != -1;
    }
    
    // Helper method to reduce loop
    private int containsKeyHelper(K key) {
    		if (this.size != 0) { 
	        for (int i = 0; i < size; i++) {
	            if ((key == null && pairs[i].key == null) || 
	            		(pairs[i].key != null && pairs[i].key.equals(key))) {
	                return i;
	            }
	        }
    		}
        return -1;
    }
    
    /**
     * @return   the size of the disc
     */
    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
	@Override
	public Iterator<KVPair<K, V>> iterator() {
		return new ArrayDictionaryIterator<K, V>(this.size, this.pairs);
	}
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private int index;
        private int size;
        private Pair<K, V>[] pairs;
        
        public ArrayDictionaryIterator(int size, Pair<K, V>[] pairs) {
            // You do not need to make any changes to this constructor.
            this.pairs = pairs;
            this.size = size;
            this.index = -1;
        }
        
		@Override
		public boolean hasNext() {
			return ((index+1) < size);
		}
	
		@Override
		public KVPair<K, V> next() {
			if (hasNext()) {
				index++;
				return new KVPair<K, V>(pairs[index].key, pairs[index].value);
			} else {
				throw new NoSuchElementException("No more element in the list");
			}
		}
    }

}
