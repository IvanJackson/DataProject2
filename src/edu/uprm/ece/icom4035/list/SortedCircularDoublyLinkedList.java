package edu.uprm.ece.icom4035.list;

import java.util.Iterator;

public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {
	int currentSize;
	Node<E> head;

	//Node class
	private static class Node<E> {

		private E element;
		private Node<E> next;
		private Node<E> Prev;

		public Node<E> getPrev() {
			return Prev;
		}

		public void setPrev(Node<E> Prev) {
			this.Prev = Prev;
		}

		public Node() {
			this.element = null;
			this.next = null;
			this.Prev = null;
		}

		public Node(E e, Node<E> prev, Node<E> next) {
			this.element = e;
			this.next = next;
			this.Prev = prev;
		}

		public E getElement() {
			return element;
		}

		public void setElement(E element) {
			this.element = element;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			this.setElement(null);
			this.setNext(null);
			this.setPrev(null);
		}

	}

	//Backwards Iterator Class
	private class BackwardsIterator implements ReverseIterator<E> {
		Node<E> current;

		public BackwardsIterator(Node<E> head) {
			this.current = head;
		}

		@Override
		public boolean hasPrevious() {
			return current.getPrev().getElement() != null;
		}

		@Override
		public E previous() {
			if (hasPrevious()) {
				current = current.getPrev();
				return current.getElement();
			} 
			else return null;
		}
	}
	
	//Forward Iterator Class
	private class ForwardIterator implements Iterator<E> {
		Node<E> current;

		public ForwardIterator(Node<E> head) {
			this.current = head;
		}

		@Override
		public boolean hasNext() {
			return current.getNext().getElement() != null;
		}

		@Override
		public E next() {
			if (hasNext()) {
				current = current.getNext();
				return current.getElement();
			} 
			else return null;
		}
	}
	
	//Constructor
	public SortedCircularDoublyLinkedList() {
		head = new Node<E>();
		currentSize = 0;
	}

	//Helper Method
	private Node<E> findNode(int index) throws IndexOutOfBoundsException {
		Node<E> current = head.getNext();
		int counter = 0;
		if (index > this.size() || index < 0)
			throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		while (current != head) {
			if (counter == index)
				return current;
			current = current.getNext();
			counter++;
		}
		return null;
	}
	
	//Instance Methods
	
	@Override
	public boolean add(E obj) {
		//If the list is empty, there is no need to compare elements, just add it
		if (this.size() == 0) {
			head.setNext(new Node<E>(obj, head, head));  
			head.setPrev(head.getNext());
			this.currentSize++;
			return true;
		}
		//If the list is not empty, compare the element of the first node with the element of the second
		//And continue to compare until you reach the end of the list or find an element smaller than the one 
		//We wish to insert
		Node<E> current = head.getNext();
		while (current != head) {
			if (obj.compareTo(current.getElement()) >= 0 && current.getNext() != head)
				current = current.getNext();
			else {
				Node<E> nta2 = new Node<E>(obj,
						(obj.compareTo(current.getElement()) < 0) ? current.getPrev() : current,
						(obj.compareTo(current.getElement()) < 0) ? current : head);
				nta2.getPrev().setNext(nta2);
				nta2.getNext().setPrev(nta2);
				this.currentSize++;
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean remove(E obj) {
		//Verifies if the list contains the object to erase, if it does, uses the method firstIndex to find it
		//And utilizes the other remove method
		if (this.contains(obj))  
			return remove(this.firstIndex(obj));
		return false;
	}

	@Override
	public boolean remove(int index) throws IndexOutOfBoundsException{
		//Throws exception if the index is invalid
		if (index >= this.size()) throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		//Uses helper method to find a node on that index, connects the previous and next node, clears the target node
		Node<E> ntr = findNode(index);
		ntr.getPrev().setNext(ntr.getNext());
		ntr.getNext().setPrev(ntr.getPrev());
		ntr.clear();
		this.currentSize--;
		return true;
	}

	@Override
	public int removeAll(E obj) {
		//Checks through every node in the list and compares the target element with the node's element
		//If it matches, removes it and adds to the counter to return
		Node<E> current = head.getNext();
		int copiesErased = 0;
		while (current != head) {
			if (current.getElement() == obj) {
				Node<E> newNext = current.getNext();
				current.getPrev().setNext(newNext);
				newNext.setPrev(current.getPrev());
				current.clear();
				current = newNext;
				this.currentSize--;
				copiesErased++;
			} else
				current = current.getNext();
		}
		return copiesErased;
	}

	@Override
	public E first() {
		return (this.size()==0)? null:head.getNext().getElement();
	}

	@Override
	public E last() {
		return (this.size()==0)? null:head.getPrev().getElement();
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		//Verifies if the index is valid, if not, throws exception
		if (index > this.size() || index < 0)
			throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		//Uses helper method to find the node and then return it's element
		return findNode(index).getElement();
	}

	@Override
	public void clear() {
		//Loops through the list clearing every node
		Node<E> current = head.getNext();
		while (current != head) {
			current = current.getNext();
			current.getPrev().clear();
			this.currentSize--;
		}
	}

	@Override
	public boolean contains(E e) {
		//Uses firstIndex to verify if there exists a node with that element
		return this.firstIndex(e) == -1?  false:true;
	}

	@Override
	public boolean isEmpty() {
		return this.currentSize == 0;
	}

	@Override
	public int firstIndex(E e) {
		//Goes through the list and return the index of the first node with that elements
		//If not found, returns -1
		Node<E> current = head.getNext();
		int index = 0;
		while (current != head) {
			if (current.getElement() == e) return index;
			else {
				current = current.getNext();
				index++;
			}
		}
		return -1;
	}

	@Override
	public int lastIndex(E e) {
		//Same as firstIndex but in reverse
		Node<E> current = head.getNext();
		int index = 0;
		int itr = -1;
		while (current != head) {
			if (current.getElement() == e) {
				current = current.getNext();
				itr = index++;
			} 
			else {
				current = current.getNext();
				index++;
			}
		}
		return itr;
	}

	@Override
	public Iterator<E> iterator(int index) {
		//Returns a new forward iterator that starts from the index until the head
		Node<E> current = this.findNode(index);
		return new ForwardIterator(current);
	}
	
	@Override
	public Iterator<E> iterator() {
		//Returns a forward iterator that starts from the head
		return new ForwardIterator(head);
	}
	
	@Override
	public ReverseIterator<E> reverseIterator(int index) {
		//Returns a new backwards iterator that starts from the index until the head
		Node<E> current = this.findNode(index);
		return new BackwardsIterator(current);
	}

	@Override
	public ReverseIterator<E> reverseIterator() {
		//Returns a backward iterator that starts at the head
		return new BackwardsIterator(head);
	}
}
