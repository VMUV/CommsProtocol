package comms.protocol.java.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.DataPacket;
import comms.protocol.java.Endianness;
import comms.protocol.java.Rotation_Vector_RawDataPacket;
import comms.protocol.java.SerializeUtilities;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;

public class Rotation_Vector_RawDataPacketUnitTests 
{
	byte[] testBytePayload = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    float[] testFloatPayload = new float[] { 0, 1, 2, 3 };
	
	@Test
	public void RotationVector_TestConstructor() throws Exception
    {
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        Rotation_Vector_RawDataPacket packet1 = new Rotation_Vector_RawDataPacket(testBytePayload);
        DataPacket dPacket = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
            (short)testBytePayload.length, testBytePayload);
        Rotation_Vector_RawDataPacket packet2 = new Rotation_Vector_RawDataPacket(dPacket);

        assertEquals(ValidPacketTypes.rotation_vector_raw_data_packet,
            packet.getPacketType());
        assertEquals(testBytePayload.length, packet.getExpectedLen());
        assertEquals(ValidPacketTypes.rotation_vector_raw_data_packet,
            packet1.getPacketType());
        assertEquals(testBytePayload.length, packet1.getExpectedLen());
        assertEquals(ValidPacketTypes.rotation_vector_raw_data_packet,
                        packet2.getPacketType());
        assertEquals(testBytePayload.length, packet2.getExpectedLen());

        for (int i = 0; i < testBytePayload.length; i++)
        {
        	assertEquals(testBytePayload[i], packet1.getPayload()[i]);
        	assertEquals(testBytePayload[i], packet2.getPayload()[i]);
        }

    }
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void RotationVector_TestInvalidConstructor() throws Exception
    {
		thrown.expect(Exception.class);
		byte[] invalidBytePayload = new byte[12];
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket(invalidBytePayload);
    }
	
	@Test
	public void RotationVector_PacketTestInvalidConstructor2() throws Exception
    {
		thrown.expect(Exception.class);
		byte[] invalidBytePayload = new byte[12];
        DataPacket dPacket = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
            (short)invalidBytePayload.length, invalidBytePayload);

        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket(dPacket);
        assertEquals(ValidPacketTypes.rotation_vector_raw_data_packet, packet.getPacketType());
        assertEquals(testBytePayload.length, packet.getExpectedLen());

        DataPacket dPacket1 = new DataPacket(ValidPacketTypes.rotation_vector_raw_data_packet,
            (short)testBytePayload.length, invalidBytePayload);
        Rotation_Vector_RawDataPacket packet1 = new Rotation_Vector_RawDataPacket(dPacket1);
    }
	
	@Test
	public void Motus_1_PacketTestSerializeInvalidPayload() throws Exception
    {
        thrown.expect(Exception.class);
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        byte[] invalidBytePayload = new byte[12];
        packet.Serialize(invalidBytePayload);
    }
	
	@Test
	public void RotationVector_PacketTestSerializeBytes() throws Exception
    {
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();

        packet.Serialize(testBytePayload);
        byte[] rtnPayload = packet.getPayload();
        float[] rtnFloatPayload = packet.DeSerialize();

        assertEquals(packet.getExpectedLen(), rtnPayload.length);
        assertEquals(packet.getExpectedLen() / 4, rtnFloatPayload.length);

        for (int i = 0; i < rtnPayload.length; i++)
        	assertEquals(testBytePayload[i], rtnPayload[i]);

        float[] known = new float[packet.getExpectedLen() / 4];
        int byteIndex = 0;
        for (int i = 0; i < known.length; i++)
        {
            byte[] tmp = new byte[4];
            tmp[0] = rtnPayload[byteIndex++];
            tmp[1] = rtnPayload[byteIndex++];
            tmp[2] = rtnPayload[byteIndex++];
            tmp[3] = rtnPayload[byteIndex++];
            known[i] = SerializeUtilities.ConvertByteArrayToFloat(tmp, Endianness.little_endian);
        }
        for (int i = 0; i < rtnFloatPayload.length; i++)
        	assertEquals(known[i], rtnFloatPayload[i], .001);
    }
	
	@Test
	public void RotationVectorPacket_TestSerializeFloats()
    {
		Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        float[] rawSensorData = {(float) .5, (float) .6, (float) .7, (float) .8};
        byte[] knownPayload = new byte[16];
        int indexToInsertElement = 0;
        for (int i = 0; i < rawSensorData.length; i++)
        	indexToInsertElement = SerializeUtilities.BufferFloatInToByteArray(rawSensorData[i], knownPayload, indexToInsertElement, Endianness.little_endian);

        try 
        {
			packet.Serialize(rawSensorData);
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        byte[] rtnPayload = packet.getPayload();

        assertEquals(packet.getExpectedLen(), rtnPayload.length);
        for (int i = 0; i < rtnPayload.length; i++)
        	assertEquals(rtnPayload[i], knownPayload[i]);
    }
	
	@Test
	public void RotationVectorPacket_TestDeserialize()
	{
		Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        float[] rawSensorData = {(float) .5, (float) .6, (float) .7, (float) .8};
        
        try 
        {
			packet.Serialize(rawSensorData);
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        float[] rtnFloatArray = packet.DeSerialize();
        
        for (int i = 0; i < rtnFloatArray.length; i++)
        	assertEquals(rtnFloatArray[i], rawSensorData[i], .001);
	}
}
