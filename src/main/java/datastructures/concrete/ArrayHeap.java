package datastructures.concrete;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    
    private T[] heap;
    private int size;
    // Feel free to add more fields and constants.
    
    public ArrayHeap() {
        heap = this.makeArrayOfT(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
    		if (size == 0) {
    			throw new EmptyContainerException("queue is empty");
    		}
        T result = heap[0];
        heap[0] = heap[size - 1];
        size--;
        this.removeMinHelper(0);
        return result;
    }
    
    private void removeMinHelper(int index) {
    		int minIndex = this.findMinChildren(index);
    		if (minIndex != index) {
    			T temp = heap[index];
    			heap[index] = heap[minIndex];
    			heap[minIndex] = temp;
    			removeMinHelper(minIndex);
    		}
    }
    
    private int findMinChildren(int index) {
    		int min = index;
    		int i = 1;
    		while ((NUM_CHILDREN * index +i) < size && i <= NUM_CHILDREN) {
    			if (heap[NUM_CHILDREN * index + i].compareTo(heap[min]) < 0) {
    				min = NUM_CHILDREN * index + i;
    			}
    			i++;
    		}
    		return min;
    }

    @Override
    public T peekMin() {
    		if (size == 0) {
			throw new EmptyContainerException("queue is empty");
		}
        return heap[0];
    }

    @Override
    public void insert(T item) {
    		if (item == null) {
    			throw new IllegalArgumentException("No null item allowed");
    		}
    		ensureCapacity();
    		heap[size] = item;
    		insertHelper(size);
    		size++;
    }
    
    private void insertHelper(int index) {
    		if (index != 0) { 
	    		int parentIndex = (index - 1) / NUM_CHILDREN;
	    		if (heap[parentIndex].compareTo(heap[index]) > 0) {
	    			T temp = heap[parentIndex];
	    			heap[parentIndex] = heap[index];
	    			heap[index] = temp;
	    			insertHelper(parentIndex);
	    		}
    		}
    }
    
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
    		if (this.size >= this.heap.length) {
			T[] newHeap = (T[]) (new Comparable[this.heap.length * 2]);
    			for (int i = 0; i < this.heap.length; i++) {
    				newHeap[i] = heap[i];
    			}
    			this.heap = newHeap;
    		}
    }

    @Override
    public int size() {
        return this.size;
    }
}
