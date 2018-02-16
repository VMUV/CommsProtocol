package comms.protocol.java.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.Endianness;
import comms.protocol.java.Motus_1_RawDataPacket;
import comms.protocol.java.SerializeUtilities;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;

public class Motus_1_RawDataPacketTests 
{
	//checks that the packet constructor sets the type and expected length to the correct values
	@Test
	public void Motus_1_PacketTestConstructor()
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        assertEquals(ValidPacketTypes.motus_1_raw_data_packet,
            packet.getPacketType());
        assertEquals(18, packet.getExpectedLen());
    }
	
	//defining the exception that we are expecting the serialize 
	//function to throw when we pass it a payload that is the wrong size
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//testing that our serialize function will detect bad payloads
	@Test
	public void Motus_1_PacketTestSerializeInvalidPayload() throws Exception
    {
        thrown.expect(Exception.class);
		Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        byte[] invalidBytePayload = new byte[12];
        packet.Serialize(invalidBytePayload);
    }
	
	//tests that serializing a payload does not fuck up the data being stored in any way
	@Test
	public void Motus_1_PacketTestSerializeBytes()
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        byte[] payload = new byte[packet.getExpectedLen()];
        short[] intPayload = new short[packet.getExpectedLen() / 2];

        int j = 0;
        for (byte i = 0; i < packet.getExpectedLen(); i++)
        {
            if ((i % 2) == 0)
                payload[i] = (byte)j++;
            else
                payload[i] = 0;
        }

        for (int i = 0; i < packet.getExpectedLen() / 2; i++)
            intPayload[i] = (short)i;

        try 
        {
			packet.Serialize(payload);
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] rtnPayload = packet.getPayload();
        short[] rtnIntPayload = packet.DeSerialize();

        assertEquals(packet.getExpectedLen(), rtnPayload.length);
        assertEquals(packet.getExpectedLen() / 2, rtnIntPayload.length);

        for (int i = 0; i < rtnPayload.length; i++)
            assertEquals(payload[i], rtnPayload[i]);

        for (int i = 0; i < rtnIntPayload.length; i++)
            assertEquals(intPayload[i], rtnIntPayload[i]);
    }
	
	//tests to see that the to string method really does convert each item of the payload
	//to a string and append them all together with commas between each item
	@Test
	public void TestMotus1PacketToString()
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        byte[] payload = new byte[packet.getExpectedLen()];

        int j = 0;
        for (byte i = 0; i < packet.getExpectedLen(); i++)
        {
            if ((i % 2) == 0)
                payload[i] = (byte)j++;
            else
                payload[i] = 0;
        }

        try 
        {
			packet.Serialize(payload);
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        short[] intPayload = packet.DeSerialize();
        String known = Short.toString(intPayload[0]);
        for (int i = 1; i < intPayload.length; i++)
        {
            known += ",";
            known += Short.toString(intPayload[i]);
        }

        assertEquals(known, packet.ToString());
    }
}
