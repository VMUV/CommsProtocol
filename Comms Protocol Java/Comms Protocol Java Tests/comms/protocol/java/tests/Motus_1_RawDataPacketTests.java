package comms.protocol.java.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.DataPacket;
import comms.protocol.java.Endianness;
import comms.protocol.java.Motus_1_RawDataPacket;
import comms.protocol.java.SerializeUtilities;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;

public class Motus_1_RawDataPacketTests 
{
	byte[] testBytePayload = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
    short[] testShortPayload = new short[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	
	@Test
	public void Motus_1_PacketTestConstructor() throws Exception
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        Motus_1_RawDataPacket packet1 = new Motus_1_RawDataPacket(testBytePayload);
        DataPacket dPacket = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet, 
            (short)testBytePayload.length, testBytePayload);
        Motus_1_RawDataPacket packet2 = new Motus_1_RawDataPacket(dPacket);

        assertEquals(ValidPacketTypes.motus_1_raw_data_packet, packet.getPacketType());
        assertEquals(18, packet.getExpectedLen());
        assertEquals(ValidPacketTypes.motus_1_raw_data_packet, packet1.getPacketType());
        assertEquals(18, packet1.getExpectedLen());
        assertEquals(ValidPacketTypes.motus_1_raw_data_packet, packet2.getPacketType());
        assertEquals(18, packet2.getExpectedLen());

        for (int i = 0; i < testBytePayload.length; i++)
        {
        	assertEquals(testBytePayload[i], packet1.getPayload()[i]);
        	assertEquals(testBytePayload[i], packet2.getPayload()[i]);
        }

    }
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void Motus_1_PacketTestInvalidConstructor() throws Exception
    {
		thrown.expect(Exception.class);
		byte[] invalidBytePayload = new byte[12];
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(invalidBytePayload);
    }
	
	@Test
	public void Motus_1_PacketTestInvalidConstructor2() throws Exception
    {
		thrown.expect(Exception.class);
		byte[] invalidBytePayload = new byte[12];
        DataPacket dPacket = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet, 
            (short)invalidBytePayload.length, invalidBytePayload);

        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket(dPacket);
        assertEquals(ValidPacketTypes.motus_1_raw_data_packet, packet.getPacketType());
        assertEquals(18, packet.getExpectedLen());

        DataPacket dPacket1 = new DataPacket(ValidPacketTypes.motus_1_raw_data_packet,
            (short) 18, invalidBytePayload);
        Motus_1_RawDataPacket packet1 = new Motus_1_RawDataPacket(dPacket1);
    }
	
	@Test
	public void Motus_1_PacketTestSerializeInvalidPayload() throws Exception
    {
        thrown.expect(Exception.class);
		Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();
        byte[] invalidBytePayload = new byte[12];
        packet.Serialize(invalidBytePayload);
    }
	
	@Test
	public void Motus_1_PacketTestSerializeBytes() throws Exception
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();

        packet.Serialize(testBytePayload);
        byte[] rtnPayload = packet.getPayload();
        short[] rtnShortPayload = packet.DeSerialize();

        assertEquals(packet.getExpectedLen(), rtnPayload.length);
        assertEquals(packet.getExpectedLen() / 2, rtnShortPayload.length);

        for (int i = 0; i < rtnPayload.length; i++)
        	assertEquals(testBytePayload[i], rtnPayload[i]);

        short[] known = new short[packet.getExpectedLen() / 2];
        int byteIndex = 0;
        for (int i = 0; i < known.length; i++)
        {
            byte[] tmp = new byte[] { rtnPayload[byteIndex++], rtnPayload[byteIndex++] };
            known[i] = SerializeUtilities.ConvertByteArrayToShort(tmp, Endianness.little_endian);
        }
        for (int i = 0; i < rtnShortPayload.length; i++)
        	assertEquals(known[i], rtnShortPayload[i]);
    }
	
	@Test
	public void Motus_1_PacketTestSerializeShorts() throws Exception
    {
        Motus_1_RawDataPacket packet = new Motus_1_RawDataPacket();

        packet.Serialize(testShortPayload);
        byte[] rtnPayload = packet.getPayload();
        short[] rtnShortPayload = packet.DeSerialize();

        assertEquals(packet.getExpectedLen(), rtnPayload.length);
        assertEquals(packet.getExpectedLen() / 2, rtnShortPayload.length);

        for (int i = 0; i < rtnShortPayload.length; i++)
        	assertEquals(testShortPayload[i], rtnShortPayload[i]);
    }
}
