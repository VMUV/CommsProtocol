package comms.protocol.java.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.SimpleQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Rule;

public class SimpleQueueUnitTests 
{
	@Test
	public void SimpleQueue_TestEnqueueSize()
	{
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		ints.enqueue(new Integer(6));
		assertEquals(ints.size(), 1);
		ints.enqueue(new Integer(7));
		assertEquals(ints.size(),2);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void SimpleQueue_TestEnqueueOverflow() throws IllegalStateException
	{
		thrown.expect(IllegalStateException.class);
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		for (int i = 0; i < 6; i++)
			ints.enqueue(new Integer(i));
	}
	
	@Test
	public void SimpleQueue_TestDequeueValuesAndSize()
	{
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		ints.enqueue(new Integer(6));
		ints.enqueue(new Integer(7));
		int deq = ints.dequeue();
		assertEquals(deq, 6);
		assertEquals(ints.size(), 1);
		deq = ints.dequeue();
		assertEquals(deq, 7);
		assertEquals(ints.size(), 0);
	}
	
	@Test
	public void SimpleQueue_TestDequeueUnderflow() throws NoSuchElementException
	{
		thrown.expect(NoSuchElementException.class);
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		int deq = ints.dequeue();
	}
	
	@Test
	public void SimpleQueue_TestIsEmpty()
	{
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		assertTrue(ints.isEmpty());
		ints.enqueue(new Integer(4));
		assertTrue(!ints.isEmpty());
	}
	
	@Test
	public void SimpleQueue_TestGetMaxSize()
	{
		SimpleQueue<Integer> ints = new SimpleQueue<>(5);
		assertEquals(ints.getMaxSize(), 5);
	}
	
}
