package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
	
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        
        try {
        		IList<Integer> top = Searcher.topKSort(-1, list);
        		fail("Need to throw excption for k < -1");
        } catch (IllegalArgumentException exp) {
        		// Good
        }
    }
    
    @Test(timeout=SECOND)
    public void testKisZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testKLargerThanSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(22, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSameNumber() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 40; i++) {
            list.add(5);
        }

        IList<Integer> top = Searcher.topKSort(20, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(5, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testLargeToSmallToLarge() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i <= 10; i++) {
            list.add(i);
        }
        
        for (int i = 10; i > 0; i--) {
            list.add(i);
        }
 
        IList<Integer> top = Searcher.topKSort(6, list);
        assertEquals(6, top.size());
        
        assertEquals(10, top.get(5));
        assertEquals(10, top.get(4));
        assertEquals(9, top.get(3));
        assertEquals(9, top.get(2));
        assertEquals(8, top.get(1));
        assertEquals(8, top.get(0));
    }
}
