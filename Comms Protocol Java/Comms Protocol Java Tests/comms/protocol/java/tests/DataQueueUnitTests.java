package comms.protocol.java.tests;

import org.junit.Test;

import comms.protocol.java.DataPacket;
import comms.protocol.java.DataQueue;
import comms.protocol.java.Motus_1_RawDataPacket;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataQueueUnitTests 
{
	private byte[] sampleMotusPayload = new byte[] {0, 1, 2, 3, 4, 5, 6,
										            7, 8, 9, 10, 11, 12, 13,
										            14, 15, 16, 17};

	private void FillQueue(DataQueue queue, DataPacket packet)
	{
		for (int i = 0; i < queue.getMaxSize(); i++)
			queue.Add(packet);
	}
	
	@Test
	public void DataQueue_TestConstructor()
    {
        DataQueue defaultQueue = new DataQueue();
        DataQueue queue = new DataQueue(128);
        assertEquals(64, defaultQueue.getMaxSize());
        assertEquals(128, queue.getMaxSize());
    }
	
	@Test
	public void DataQueue_TestIsEmpty()
    {
        DataQueue queue = new DataQueue();
        assertTrue(queue.IsEmpty());
    }
	
	@Test
	public void DataQueue_TestAddPacket() throws Exception
    {
		Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();

        assertTrue(queue.Add(packet));
        assertTrue(!queue.IsEmpty());
    }
	
	@Test
	public void DataQueue_TestAddOverFlowProtection() throws Exception
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();

        for (int i = 0; i < queue.getMaxSize(); i++)
            assertTrue(queue.Add(packet));

        assertTrue(!queue.Add(packet));
    }
	
	@Test
	public void DataQueue_TestGet() throws Exception
    {
		Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();
        queue.Add(packet);

        DataPacket getPacket = queue.Get();
        assertEquals(packet.getPacketType(), getPacket.getPacketType());
        assertEquals(packet.getExpectedLen(), getPacket.getExpectedLen());
        for (int i = 0; i < packet.getExpectedLen(); i++)
            assertEquals(packet.getPayload()[i], getPacket.getPayload()[i]);
    }
	
	@Test
	public void DataQueue_TestGetUnderFlowProtection() throws Exception
    {
		Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();

        FillQueue(queue, packet);

        int count;
        for (count = 0; !queue.IsEmpty(); count++)
        {
            DataPacket getPacket = queue.Get();
            assertEquals(packet.getPacketType(), getPacket.getPacketType());
            assertEquals(packet.getExpectedLen(), getPacket.getExpectedLen());
            for (int j = 0; j < packet.getExpectedLen(); j++)
                assertEquals(packet.getPayload()[j], getPacket.getPayload()[j]);
        }

        assertEquals(count, queue.getMaxSize());
        assertTrue(queue.IsEmpty());
        DataPacket badPacket = queue.Get();
        assertEquals(ValidPacketTypes.end_valid_packet_types, badPacket.getPacketType());
        assertEquals(-1, badPacket.getExpectedLen());
    }
	
	@Test
	public void DataQueue_TestGetStreamable() throws Exception
    {
        byte[] stream = new byte[2048];
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();
        int expectedNumBytesToQueue = ((18 + DataPacket.NumOverHeadBytes) * queue.getMaxSize());

        FillQueue(queue, packet);
        int numBytesQueued = queue.GetStreamable(stream, stream.length);
        assertEquals(expectedNumBytesToQueue, numBytesQueued);
        assertTrue(queue.IsEmpty());

        for (int i = 0; i < numBytesQueued; )
        {
            DataPacket rebuilt = new DataPacket();
            i += rebuilt.Serialize(stream, i);
            assertEquals(packet.getPacketType(), rebuilt.getPacketType());
            assertEquals(packet.getExpectedLen(), rebuilt.getExpectedLen());
            for (int j = 0; j < rebuilt.getExpectedLen(); j++)
                assertEquals(packet.getPayload()[j], rebuilt.getPayload()[j]);
        }
    }
	
	@Test
	public void DataQueue_TestParseStreamable() throws Exception
    {
        byte[] stream = new byte[2048];
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        packet.Serialize(sampleMotusPayload);
        DataQueue queue = new DataQueue();

        FillQueue(queue, packet);
        int numBytesQueued = queue.GetStreamable(stream, stream.length);
        FillQueue(queue, packet);

        DataQueue queue2 = new DataQueue();
        queue2.ParseStreamable(stream, stream.length);

        while (!queue2.IsEmpty() && !queue.IsEmpty())
        {
            DataPacket packet1 = queue.Get();
            DataPacket packet2 = queue2.Get();

            // Ensure packet1 is the same as packet
            assertEquals(packet.getPacketType(), packet1.getPacketType());
            assertEquals(packet.getExpectedLen(), packet1.getExpectedLen());
            for (int j = 0; j < packet1.getExpectedLen(); j++)
                assertEquals(packet.getPayload()[j], packet1.getPayload()[j]);

            // Ensure packet2 is the same as packet1
            assertEquals(packet1.getPacketType(), packet2.getPacketType());
            assertEquals(packet1.getExpectedLen(), packet2.getExpectedLen());
            for (int j = 0; j < packet2.getExpectedLen(); j++)
                assertEquals(packet1.getPayload()[j], packet2.getPayload()[j]);
        }
    }
}
