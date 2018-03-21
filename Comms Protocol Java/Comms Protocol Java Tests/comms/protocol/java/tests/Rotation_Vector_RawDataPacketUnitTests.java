package comms.protocol.java.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import comms.protocol.java.Endianness;
import comms.protocol.java.Rotation_Vector_RawDataPacket;
import comms.protocol.java.SerializeUtilities;
import comms.protocol.java.ValidPacketTypes;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;

public class Rotation_Vector_RawDataPacketUnitTests 
{
	@Test
	public void RotationVectorPacket_TestConstructor()
    {
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        assertEquals(ValidPacketTypes.rotation_vector_raw_data_packet, packet.getPacketType());
        assertEquals(16, packet.getExpectedLen());
    }
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void Motus_1_PacketTestSerializeInvalidPayload() throws Exception
    {
        thrown.expect(Exception.class);
        Rotation_Vector_RawDataPacket packet = new Rotation_Vector_RawDataPacket();
        float[] invalidBytePayload = new float[2];
        packet.Serialize(invalidBytePayload);
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
