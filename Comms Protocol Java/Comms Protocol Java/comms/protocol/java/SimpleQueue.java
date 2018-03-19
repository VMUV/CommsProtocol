package comms.protocol.java;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SimpleQueue<E>
{

	private LinkedList<E> list;
	private int maxSize;
	
	public SimpleQueue(int size)
	{
		list = new LinkedList<E>();
		maxSize = size;
	}

	public boolean enqueue(E o) throws IllegalStateException
	{
		if (list.size() < this.getMaxSize())
		{
			list.addLast(o);
			return true;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
  
	public E dequeue() throws NoSuchElementException
	{
		if (list.isEmpty())
		{
			throw new NoSuchElementException();
		}
		return list.removeFirst();
	}

	public boolean isEmpty() 
	{
		return list.isEmpty();
	}

	public int size() 
	{
		return list.size();
	}
	
	public int getMaxSize()
	{
		return maxSize;
	}
}