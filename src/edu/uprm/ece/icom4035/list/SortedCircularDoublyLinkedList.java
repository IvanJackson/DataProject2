package edu.uprm.ece.icom4035.list;

import java.util.Iterator;


public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {
	int size;
	Node<E> head;
	
	private static class Node<E>{
			
			private E element;
			private Node<E> next;
			private Node<E> previous;
			
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
			
			public Node(E e, Node<E> prev, Node<E> next) {
				this.element = e;
				this.next = next;
				this.previous = prev;
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
				this.setPrevious(null);
			}
		}

	public SortedCircularDoublyLinkedList(){
		head = new Node<E>();
		size=0;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new ForwardIterator(head);
	}

	@Override
	public boolean add(E obj) {
		if(this.size()==0) {
			head.setNext(new Node<E>(obj, head, head));
			head.setPrevious(head.getNext());
			this.size++;
			return true;
		}
		Node<E> current = head.getNext();
		while(current!=head) {
			if(obj.compareTo(current.getElement())<0||current.getNext()==head){
//				Node<E> nta = new Node<E>(obj, (current.getNext()==head)?current:current.getPrevious(), (current.getNext()==head)?head:current);
				Node<E> nta = new Node<E>(obj, (obj.compareTo(current.getElement())<0)?current.getPrevious():current,
						(obj.compareTo(current.getElement())<0)?current:head);
				current.setNext(nta);
				this.size++;
				if(nta.getNext()==head) head.setPrevious(nta);
				return true;
			}
			else if(obj.compareTo(current.getElement())>=0) current = current.getNext();
		}
		return false;
//		while(obj.compareTo(current.getElement())>0) {
//			current = current.getNext();
//		}
//		Node<E> nta = new Node<E>(obj, current, current.getNext());
//		current.setNext(nta);
//		if(nta.getNext()!=null) nta.getNext().setPrevious(nta);
//		this.size++;
//		return true;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean remove(E obj) {
		if(this.contains(obj)) remove(this.firstIndex(obj));
		this.size--;
		return false;
	}

	@Override
	public boolean remove(int index) {
		if(index>=this.size()) return false;
		Node<E> ntr = findNode(index);
		ntr.getPrevious().setNext(ntr.getNext());
		ntr.getNext().setPrevious(ntr.getPrevious());
		ntr.clear();
		this.size--;
		return true;
	}

	@Override
	public int removeAll(E obj) {
		Node<E> current =head.getNext();
		int copiesErased=0;
		while(current!=head) {
			if(current.getElement()==obj) {
				Node<E> newNext=current.getNext();
				current.getPrevious().setNext(newNext);
				newNext.setPrevious(current.getPrevious());
				current.clear();
				current = newNext;
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
		if(index>this.size()||index<0) throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		return findNode(index).getElement();
	}
	
	private Node<E> findNode(int index) throws IndexOutOfBoundsException{
		Node<E> current = head.getNext();
		int counter=0;
		if(index>this.size()||index<0) throw new IndexOutOfBoundsException("Index is greater than the size of the list");
		while(current!=head) {
			if(counter==index) return current;
			current = current.getNext();
			counter++;
		}
		return null;
	}
	
	@Override
	public void clear() {
		Node<E> current = head.getNext();
		while(current!=head) {
			current=current.getNext();
			current.getPrevious().clear();;

		}		
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
		return new ForwardIterator(current);
	}

	private class ForwardIterator implements Iterator<E>{
		Node<E> head;
		Node<E> current;
		
		public ForwardIterator(Node<E> head){
			this.head = head;
			this.current=head.getNext();
		}
		@Override
		public boolean hasNext() {
			return current!=head;
		}

		@Override
		public E next() {
			if(hasNext()) {
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
		while(current!=head) {
			if(current.getElement()==e) return index;
			index++;
		}
		return -1;
	}

	@Override
	public int lastIndex(E e) {
		Node<E> current = head.getPrevious();
		int index =0;
		while(current!=head) {
			if(current.getElement()==e) return index;
			index++;
		}
		return -1;
	}

	public Node<E> getHead() {
		return head;
	}
	
	
	private class BackwardsIterator implements ReverseIterator<E>{
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
		return new BackwardsIterator(head);
	}

	@Override
	public ReverseIterator<E> reverseIterator(int index) {
		Node<E> current = this.findNode(index);
		return new BackwardsIterator(current);
	}
	

}
