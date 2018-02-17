package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import java.util.Iterator;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test (timeout=10*SECOND)
    public void testSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5000000; i++) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(2500000, list);
        assertEquals(2500000, top.size());
        Iterator<Integer> iter = top.iterator();
        for (int i = 0; i < top.size(); i++) {
        		assertEquals(2500000+i, iter.next());
        }  
    }
    
    @Test (timeout=10*SECOND)
    public void testHeapStress() {	
    		IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
    		for (int i = 3000000; i >= 0; i--) {
			heap.insert(i);
			assertEquals(i, heap.peekMin());
		} 
    		for (int i = 0; i <= 3000000; i++) {
    			heap.insert(i);
    			assertEquals(0, heap.peekMin());
    		}
    		for (int i = 0; i <= 6000000; i+=2) {
    			assertEquals(i/2, heap.removeMin());
    			heap.removeMin();
    		}
    }
}
