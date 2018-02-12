package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testEmptyHeapExceptions() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		try {
    			heap.peekMin();
    			fail("Fail to throw excption when heap is empty");
    		} catch (EmptyContainerException exp) {
    			// Good
    		}
    		
    		try {
    			heap.removeMin();
    			fail("Fail to throw excption when heap is empty");
    		} catch (EmptyContainerException exp) {
    			// Good
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertNull() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		try {	
    			heap.insert(null);
    			fail("Can not insert null to heap");
    		} catch (IllegalArgumentException exp) {
    			// Good
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertRemoveSameNumber() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 0; i < 12; i++) {
    			heap.insert(3);
    		} 
    		for (int i = 0; i < 12; i++) {
    			assertEquals(3, heap.removeMin());
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertSmallToLarge() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 1; i <= 5; i++) {
    			heap.insert(i);
    			assertEquals(1, heap.peekMin());
    		} 
    		
		assertEquals(1, heap.removeMin());
		assertEquals(2, heap.peekMin());
		
		assertEquals(2, heap.removeMin());
		assertEquals(3, heap.peekMin());
		
		assertEquals(3, heap.removeMin());
		assertEquals(4, heap.peekMin());
		
		assertEquals(4, heap.removeMin());
		assertEquals(5, heap.peekMin());
		
		assertEquals(5, heap.removeMin());
		try {
			heap.peekMin();
			fail("Can not peek empty heap");
		} catch (EmptyContainerException exp) {
			// good
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertLargeToSmall() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 5; i >= 1; i--) {
    			heap.insert(i);
    			assertEquals(i, heap.peekMin());
    		} 
    		
		assertEquals(1, heap.removeMin());
		assertEquals(2, heap.peekMin());
		
		assertEquals(2, heap.removeMin());
		assertEquals(3, heap.peekMin());
		
		assertEquals(3, heap.removeMin());
		assertEquals(4, heap.peekMin());
		
		assertEquals(4, heap.removeMin());
		assertEquals(5, heap.peekMin());
		
		assertEquals(5, heap.removeMin());
		try {
			heap.peekMin();
			fail("Can not peek empty heap");
		} catch (EmptyContainerException exp) {
			// good
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertRemoveDuplicate() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 5; i >= 1; i--) {
    			heap.insert(i);
    			assertEquals(i, heap.peekMin());
    		} 
    		heap.insert(5);
    		heap.insert(2);
    		heap.insert(3);
    		heap.insert(4);
    		heap.insert(1);
    		
		assertEquals(1, heap.removeMin());
		
		assertEquals(1, heap.removeMin());
		
		assertEquals(2, heap.removeMin());
		
		assertEquals(2, heap.removeMin());
		
		assertEquals(3, heap.removeMin());
		
		assertEquals(3, heap.removeMin());
		
		assertEquals(4, heap.removeMin());
		
		assertEquals(4, heap.removeMin());
		
		assertEquals(5, heap.removeMin());
		
		assertEquals(5, heap.removeMin());
		try {
			heap.peekMin();
			fail("Can not peek empty heap");
		} catch (EmptyContainerException exp) {
			// good
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertRemoveLargeToSmallToLarge() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 5; i >= 0; i--) {
			heap.insert(i);
			assertEquals(i, heap.peekMin());
		} 
    		for (int i = 0; i <= 5; i++) {
    			heap.insert(i);
    			assertEquals(0, heap.peekMin());
    		} 
    		assertEquals(0, heap.removeMin());
    		
    		assertEquals(0, heap.removeMin());
    		
    		assertEquals(1, heap.removeMin());
    		
    		assertEquals(1, heap.removeMin());
    		
    		assertEquals(2, heap.removeMin());
    		
    		assertEquals(2, heap.removeMin());
    		
    		assertEquals(3, heap.removeMin());
    
    		assertEquals(3, heap.removeMin());
    		
    		assertEquals(4, heap.removeMin());
    		
    		assertEquals(4, heap.removeMin());
    		
    		assertEquals(5, heap.removeMin());
    		
    		assertEquals(5, heap.removeMin());
    }
}
