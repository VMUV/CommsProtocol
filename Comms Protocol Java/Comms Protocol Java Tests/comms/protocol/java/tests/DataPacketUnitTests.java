package comms.protocol.java.tests;

import org.junit.Test;

import comms.protocol.java.DataPacket;
import comms.protocol.java.Motus_1_RawDataPacket;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

public class DataPacketUnitTests 
{
	short testPayloadLen = 0x0005;
    byte[] streamTest = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)ValidPacketTypes.test_packet.getValue(), (byte)(0x0005 >> 8),
                                (byte)(0x0005), 0, 1, 2, 3, 4 };
	
	@Test
	public void DataPacket_TestConstructor()
	{
		DataPacket defaultConstr = new DataPacket();
        byte[] test = new byte[] { 0, 1, 2, 3, 4 };
        DataPacket constr = new DataPacket(ValidPacketTypes.test_packet, (short)test.length, test);

        assertEquals(ValidPacketTypes.end_valid_packet_types, defaultConstr.getPacketType());
        assertEquals(-1, defaultConstr.getExpectedLen());

        assertEquals(ValidPacketTypes.test_packet, constr.getPacketType());
        assertEquals(test.length, constr.getExpectedLen());
        for (int i = 0; i < constr.getExpectedLen(); i++)
            assertEquals(constr.getPayload()[i], test[i]);
	}
	
	@Test
	public void DataPacket_TestSerializeFromStream()
    {
        DataPacket packet = new DataPacket();
        int index = packet.SerializeFromStream(streamTest, 0);
        assertEquals(streamTest.length, index);
        assertEquals(ValidPacketTypes.test_packet, packet.getPacketType());
        assertEquals(testPayloadLen, packet.getExpectedLen());
        for (int i = 0; i < packet.getExpectedLen(); i++)
        	assertEquals(packet.getPayload()[i], streamTest[DataPacket.DataStartPos + i]);
    }
	
	@Test
	public void DataPacket_TestSerializeFromStreamBadHeader()
    {
        DataPacket packet = new DataPacket();
        int offset = 1;
        offset = packet.SerializeFromStream(streamTest, offset);
        assertEquals(2, offset);
        assertEquals(ValidPacketTypes.end_valid_packet_types, packet.getPacketType());
        assertEquals(-1, packet.getExpectedLen());
    }
	
	@Test
	public void DataPacket_TestSerializeFromStreamBadLen()
    {
        byte[] badLen = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)ValidPacketTypes.test_packet.getValue(), (byte)(0x0005 >> 8),
                                (byte)(0x0005), 0, 1, 2, 3 };
        DataPacket packet = new DataPacket();
        int offset = 0;
        offset = packet.SerializeFromStream(badLen, offset);
        assertEquals(5, offset);
        assertEquals(ValidPacketTypes.end_valid_packet_types, packet.getPacketType());
        assertEquals(-1, packet.getExpectedLen());
    }
	
	@Test
	public void DataPacket_TestSerializeFromStreamBadType()
    {
        byte[] badType = new byte[] { DataPacket.Header1, DataPacket.Header2, (byte)(ValidPacketTypes.end_valid_packet_types.getValue() + 1), (byte)(0x0005 >> 8),
                                (byte)(0x0005), 0, 1, 2, 3, 4 };
        DataPacket packet = new DataPacket();
        int offset = 0;
        offset = packet.SerializeFromStream(badType, offset);
        assertEquals(3, offset);
        assertEquals(ValidPacketTypes.end_valid_packet_types, packet.getPacketType());
        assertEquals(-1, packet.getExpectedLen());
    }
	
	@Test
	public void DataPacket_TestSerializeToStream()
    {
        byte[] stream = new byte[64];
        byte[] test = new byte[] { 0, 1, 2, 3, 4 };
        DataPacket packet = new DataPacket(ValidPacketTypes.test_packet, (short)test.length, test);
        int numBytesQueued = packet.SerializeToStream(stream, 0);
        assertEquals(streamTest.length, numBytesQueued);
        assertEquals(ValidPacketTypes.test_packet, packet.getPacketType());
        assertEquals(test.length, packet.getExpectedLen());
        for (int i = 0; i < packet.getExpectedLen(); i++)
            assertEquals(packet.getPayload()[i], streamTest[DataPacket.DataStartPos + i]);
    }
	
	@Test
	public void DataPacket_TestSerializeToStreamBadType()
    {
        byte[] stream = new byte[64];
        byte[] test = new byte[] { 0, 1, 2, 3, 4 };
        DataPacket packet = new DataPacket(ValidPacketTypes.end_valid_packet_types, (short)test.length, test);
        int numBytesQueued = packet.SerializeToStream(stream, 0);
        assertEquals(0, numBytesQueued);
        assertEquals(ValidPacketTypes.end_valid_packet_types, packet.getPacketType());
        assertEquals((short)test.length, packet.getExpectedLen());
    }
	
	@Test
	public void DataPacket_TestSerializeToStreamBufferOverRun()
    {
        byte[] stream = new byte[1];
        byte[] test = new byte[] { 0, 1, 2, 3, 4 };
        DataPacket packet = new DataPacket(ValidPacketTypes.test_packet, (short)test.length, test);
        int numBytesQueued = packet.SerializeToStream(stream, 0);
        assertEquals(0, numBytesQueued);
        assertEquals(ValidPacketTypes.test_packet, packet.getPacketType());
        assertEquals((short)test.length, packet.getExpectedLen());
    }
	
}
