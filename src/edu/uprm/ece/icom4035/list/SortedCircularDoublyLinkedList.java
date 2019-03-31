package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {
	int size;
	SortedCircularDoublyLinkedList list;
	Node<E> head;
	private static class Node<E>{
			
			private E element;
			private Node<E> next;
			private Node<E> previous;
			private Node<E> head;
			
			public Node<E> getPrevious() {
				return previous;
			}
	
			public void setPrevious(Node<E> previous) {
				this.previous = previous;
			}
	
			public Node(){
				this.element = null;
				this.next = null;
				this.previous = null;
			}
			
			public Node(E e, Node<E> N, Node<E> P) {
				this.element = e;
				this.next = N;
				this.previous = P;
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
		}

	public SortedCircularDoublyLinkedList(){
		this.list = new SortedCircularDoublyLinkedList<E>();
		head = new Node<E>();
		size=0;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new ForwardIterator<E>(head);
	}

	@Override
	public boolean add(E obj) {
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean remove(E obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int removeAll(E obj) {
		Node<E> current =head;
		int copiesErased=0;
		while(current.getNext()!=head) {
			if(current.getElement()==obj) {
				Node<E> newNext=current.getNext().getNext();
				current.setNext(newNext);
				newNext.setPrevious(current);
				current = current.getNext();
				this.size--;
				copiesErased++;
			}
		}
		return copiesErased;
	}

	@Override
	public E first() {
		return head.getNext().getElement();
	}

	@Override
	public E last() {
		return head.getPrevious().getElement();
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException{
		Node<E> current = head.getNext();
		int counter=0;
		if(index>this.size()||index<0) throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		while(current.getNext()!=head) {
			if(counter==index) return current.getElement();
			current = current.getNext();
			counter++;
		}
		return null;
	}
	
	private Node<E> findNode(int index) throws IndexOutOfBoundsException{
		Node<E> current = head.getNext();
		int counter=0;
		if(index>this.size()||index<0) throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		while(current.getNext()!=head) {
			if(counter==index) return current;
			current = current.getNext();
			counter++;
		}
		return null;
	}
	@Override
	
	public void clear() {
		Node<E> current = head.getNext();
		while(current.getNext()!=head) {
			current.setElement(null);
			current.setPrevious(null);
			current=current.getNext();
		}
		current.setElement(null);
		current.setPrevious(null);
		
	}

	@Override
	public boolean contains(E e) {
		if(this.firstIndex(e)==-1) return false;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.size()==0;
	}

	@Override
	public Iterator<E> iterator(int index) {
		Node<E> current = this.findNode(index);
		return new ForwardIterator<E>(current);
	}

	private class ForwardIterator<E> implements Iterator<E>{
		Node<E> head;
		Node<E> current;
		
		public ForwardIterator(Node<E> head){
			this.head = head;
			this.current=this.head;
		}
		@Override
		public boolean hasNext() {
			return current.getNext()!=head;
		}

		@Override
		public E next() {
			if(hasNext()) {
				if(current.getElement()==null) {
					current = current.getNext();
					return current.getElement();
				}
				else
					current = current.getNext();
					return current.getPrevious().getElement();
				}
			else return null;
			}
		}
	
	@Override
	public int firstIndex(E e) {
		Node<E> current = head.getNext();
		int index =0;
		while(current.getNext()!=head) {
			if(current.getElement()==e) return index;
			index++;
		}
		return -1;
	}

	@Override
	public int lastIndex(E e) {
		Node<E> current = head.getPrevious();
		int index =0;
		while(current.getPrevious()!=head) {
			if(current.getElement()==e) return index;
			index++;
		}
		return -1;
	}

	public Node<E> getHead() {
		return head;
	}
	
	
	private class BackwardsIterator<E> implements ReverseIterator<E>{
		Node<E> head;
		Node<E> current;
		
		public BackwardsIterator(Node<E> head){
			this.head = head;
			this.current=this.head;
		}
		@Override
		public boolean hasPrevious() {
			return current.getPrevious()!=head;
		}
		
		@Override
		public E previous() {
			if(hasPrevious()) {
				if(current.getElement()==null) {
					current = current.getPrevious();
					return current.getElement();
				}
				else
					current = current.getPrevious();
					return current.getNext().getElement();
				}
			else return null;
		}
	}
	
	@Override
	public ReverseIterator<E> reverseIterator() {
		return new BackwardsIterator<E>(head);
	}

	@Override
	public ReverseIterator<E> reverseIterator(int index) {
		Node<E> current = this.findNode(index);
		return new BackwardsIterator<E>(current);
	}
	

}
