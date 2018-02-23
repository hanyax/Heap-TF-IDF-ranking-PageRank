package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
	
    private IDictionary<K, V>[] chains;
    private int totalSize;
    private int bucketSize;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
    		totalSize = 0;
    		bucketSize  = 10;
        chains = this.makeArrayOfChains(bucketSize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
    		int index = this.getIndex(key);
		IDictionary<K, V> values = chains[index];
		if (values != null) {
			return values.get(key);
		} else {
			throw new NoSuchKeyException("Element does not exist");
		}
    }

    @Override
    public void put(K key, V value) {
    		ensurePerformance();
		int index = this.getIndex(key);
		if (chains[index] == null) {
			chains[index] = new ArrayDictionary<K, V>();
		} 
		if (!this.containsKey(key)) {
			totalSize++;
		}
		chains[index].put(key, value);
    }
    
    private int getIndex(K key) {
		if (key == null) {
			return 0;
		} else {
			if (key.hashCode() < 0) {
				return -key.hashCode()  % this.bucketSize;
			}
			return key.hashCode() % this.bucketSize;
		}
    }
    
    private void ensurePerformance() {
    		if ((totalSize * 1.0 / bucketSize) > 0.5) {
    			this.bucketSize = this.bucketSize * this.bucketSize;
    			IDictionary<K, V>[] newChains = this.makeArrayOfChains(bucketSize);
    			for (KVPair<K, V> pair : this) {
    				int index = this.getIndex(pair.getKey());
    				if (newChains[index] == null) {
    					newChains[index] = new ArrayDictionary<K, V>();
    				} 
    				newChains[index].put(pair.getKey(), pair.getValue());
    			}
    			this.chains = newChains;
    		}
    }

    @Override
    public V remove(K key) {
		if (this.containsKey(key)) {
			totalSize--;
			return chains[getIndex(key)].remove(key);
		} else {
			throw new NoSuchKeyException("Element does not exist");
		}
    }

    @Override
    public boolean containsKey(K key) {
    		if (chains[this.getIndex(key)] != null) {
    			return chains[this.getIndex(key)].containsKey(key);
    		}
    		return false;
    }

    @Override
    public int size() {
        return totalSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> currentIter; 
        private int index;
        
     
        public ChainedIterator(IDictionary<K, V>[] chains) {
        		this.chains = chains;
        		this.currentIter = null;
        		this.index = 0;
        }

        @Override
        public boolean hasNext() {
        		return hasNextHelper();
        }
        
        private boolean hasNextHelper() {
        		if (index >= chains.length) {
        			return false;
        		} else if (currentIter == null) {
        			if (chains[index] != null && !chains[index].isEmpty()) {
        				currentIter = chains[index].iterator();
        				return currentIter.hasNext();
        			} else {
        				index += 1;
        				return hasNextHelper();
        			} 
        		} else {
        			if (currentIter.hasNext()) {
        				return true;
        			} else {
        				currentIter = null;
        				index += 1;
        				return hasNextHelper();
        			}
        		}
        }
                
        @Override
        public KVPair<K, V> next() {
        		if (this.hasNext()) {
        			return currentIter.next();
        		} else {
        			throw new NoSuchElementException("No more element in the list");
        		}
        }
    }
}
