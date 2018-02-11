package datastructures.concrete;

import datastructures.interfaces.IList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import misc.exceptions.EmptyContainerException;


/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;
    
    /**
     * Init a empty list.
     */
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    /**
     * add.
     * @param       item added
     * @modifies    this
     * @effect      add item to the end of list
     */
    @Override
    public void add(T item) {
       insert(size, item);
    }
    
    /**
     * remove.
     * @requires    this != empty
     * @return      the list node that is removed
     * @throws      EmptyContainerException if there is no element in the list
     */
    @Override
    public T remove() {
        if (front == null) {
            throw new EmptyContainerException(
                    "container is empty and there is no element to remove");
        } else {
            size--;
            T removed = null;
            if (front.next == null) {
                removed = front.data;
                front = null;
                back = null;
            } else {
                removed = back.data;
                back = back.prev;
                back.next = null;
            }
            return removed;
        }
    }

    /**
     * get.
     * @param   index to get
     * @require index is in the range of list
     * @return  the item at index
     * @throws  IndexOutOfBoundsException if index is out of bounce
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            if (index <= size / 2) {
                Node<T> cur = front;
                for (int i = 0; i < index; i++) {
                    cur = cur.next;
                }
                return cur.data;
            } else {
                Node<T> cur = back;
                for (int i = size - 1; i > index; i--) {
                    cur = cur.prev;
                }
                return cur.data;
            }
        }
    }
    
    /**
     * set.
     * @param index item
     * @require index is in the range of list
     * @modifies this
     * @effects reset the data at index
     * @throws IndexOutOfBoundsException if index is out of bounce
     */
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            Node<T> newNode = new Node<T>(item);
            if (index == 0) {
                newNode.next = front.next;
                if (size - 1 != 0) {
                    front.next.prev = newNode;
                } else {
                    back = newNode;
                }
                front = newNode;
            } else if (index == size - 1) {
                newNode.prev = back.prev;
                back.prev.next = newNode;
                back = newNode;
            } else {
                if (index <= size / 2) {
                    Node<T> cur = front;
                    for (int i = 0; i < index; i++) {
                        cur = cur.next;
                    }
                    newNode.prev = cur.prev;
                    newNode.next = cur.next;
                    cur.prev.next = newNode;
                    cur.next.prev = newNode;
                } else {
                    Node<T> cur = back;
                    for (int i = size - 1; i > index; i--) {
                        cur = cur.prev;
                    }
                    newNode.prev = cur.prev;
                    newNode.next = cur.next;
                    cur.prev.next = newNode;
                    cur.next.prev = newNode;
                }
            }
        }
    }
    
    /**
     * insert.
     * @param index item
     * @require index is in the range of list
     * @modifies this
     * @effects reset the data at index
     * @throws IndexOutOfBoundsException if index is out of bounce
     */
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            Node<T> newNode = new Node<T>(item);
            if (index == 0) {
                if (size == 0) {
                    this.front = newNode;
                    this.back = newNode;
                } else {
                    newNode.next = front;
                    front.prev = newNode;
                    this.front = newNode;
                }
            } else if (index == size) {
                back.next = newNode;
                newNode.prev = back;
                back = newNode;
            } else {
                if (index <= size / 2) {
                    Node<T> cur = front;
                    for (int i = 0; i < index - 1; i++) {
                        cur = cur.next;
                    }
                    newNode.next = cur.next;
                    cur.next.prev = newNode;
                    newNode.prev = cur;
                    cur.next = newNode;
                } else {
                    Node<T> cur = back;
                    for (int i = size - 1; i >= index; i--) {
                        cur = cur.prev;
                    }
                    newNode.next = cur.next;
                    cur.next.prev = newNode;
                    newNode.prev = cur;
                    cur.next = newNode;
                }
            }
            size++;
        }
    }
    
    /**
     * delete.
     * @param       index to delete
     * @require     index is in the range of list
     * @modifies    this
     * @effects     delete data at index and shift the rest of list to the left by one
     * @throws      IndexOutOfBoundsException if index is out of bounce
     * @return      the deleted node data
     */
    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            T data = null;
            if (index == 0) {
               data = front.data;
               if (size == 1) {
                   front = null;
                   back = null;
               } else {
                   front = front.next;
                   front.prev = null;
               }
            } else if (index == size - 1) {
                data = back.data;
                back = back.prev;
                back.next = null;
            } else {
                Node<T> cur = null;
                if (index <= size / 2) {
                    cur = front;
                    for (int i = 0; i < index; i++) {
                        cur = cur.next;
                    }
                    cur.prev.next = cur.next;
                    cur.next.prev = cur.prev;
                } else {
                    cur = back;
                    for (int i = size - 1; i > index; i--) {
                        cur = cur.prev;
                    }
                    cur.prev.next = cur.next;
                    cur.next.prev = cur.prev;
                }
                data = cur.data;
            }
            size--;
            return data;
        }
    }
    
    /**
     * indexOf.
     * @param   item to find index
     * @return  the index of given item, if item is not in the list, return -1
     */
    @Override
    public int indexOf(T item) {
        Node<T> cur = front;
        if (front.data.equals(item)) {
            return 0;
        } else {
            int index = 0;
            while (cur.next != null) {
                cur = cur.next;
                index++;
                if ((item == null && cur.data == null) || cur.data.equals(item)) {
                    return index;
                }
            }
            return -1;
        }
    }
    
    /**
     * size.
     * @return  size of the list
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Return true if T item exist in list, false otherwise.
     */
    @Override
    public boolean contains(T other) {
        return (indexOf(other) != -1);
    }
        
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
        
        public String toString() {
            return data.toString();
        }
        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (hasNext()) {
               T data = current.data;
               current = current.next;
               return data;
            } else {   
                throw new NoSuchElementException("No more element in the list");
            }
        }
    }
}
