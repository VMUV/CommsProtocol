package comms.protocol.java.tests;

import org.junit.Test;

import comms.protocol.java.DataPacket;
import comms.protocol.java.Motus_1_RawDataPacket;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

public class DataPacketUnitTests 
{
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
	public void DataPacket_TestSerialize()
	{
		DataPacket packet = new DataPacket();
        short testPayloadLen = 5;
        byte[] stream = new byte[] { (byte)ValidPacketTypes.test_packet.getValue(), (byte)(testPayloadLen >> 8),
                                (byte)(testPayloadLen), 0, 1, 2, 3, 4 };

        packet.Serialize(stream, 0);
        assertEquals(ValidPacketTypes.test_packet, packet.getPacketType());
        assertEquals(testPayloadLen, packet.getExpectedLen());
        for (int i = 0; i < packet.getExpectedLen(); i++)
            assertEquals(packet.getPayload()[i], stream[DataPacket.DataStartPos + i]);
	}
	
	@Test
	public void DataPacket_compareTo_testOrderings()
	{
		//let the number of packet types(testPacket, motusPacket, etc) be the integer n
		DataPacket testPacket = new DataPacket(ValidPacketTypes.test_packet, (short)5, new byte[] {1,2,3,4,5});
		DataPacket motusPacket = new Motus_1_RawDataPacket();
		
		//there will be n^2 asserts
		assertEquals(testPacket.compareTo(testPacket), 0);
		assertEquals(testPacket.compareTo(motusPacket), -1);
		assertEquals(motusPacket.compareTo(motusPacket), 0);
		assertEquals(motusPacket.compareTo(testPacket), 1);
		
	}
	
}
