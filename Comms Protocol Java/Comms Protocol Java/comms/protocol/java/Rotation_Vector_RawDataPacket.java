package comms.protocol.java;

public class Rotation_Vector_RawDataPacket extends DataPacket
{
	public Rotation_Vector_RawDataPacket()
    {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, (short)16, new byte[0]);
    }
	
	public void Serialize(float[] rawSensorData) throws Exception
    {
		if (rawSensorData.length < 4)
			throw new Exception();
		byte[] payload = new byte[16];
		int indexToInsertElement = 0;
		for (int i = 0; i < 4; i++)
		{
			indexToInsertElement = SerializeUtilities.BufferFloatInToByteArray(rawSensorData[i], payload, indexToInsertElement, Endianness.little_endian);
		}

        this.setPayload(payload);
    }
	
	public float[] DeSerialize()
    {
        float[] rtn = new float[this.getExpectedLen() / 4];
        int byteIndex = 0;
        byte[] bytePayload = this.getPayload();
        try
        {
            for (int i = 0; i < rtn.length; i++)
            {
                byte[] tmp = new byte[4];
                tmp[0] = bytePayload[byteIndex++];
                tmp[1] = bytePayload[byteIndex++];
                tmp[2] = bytePayload[byteIndex++];
                tmp[3] = bytePayload[byteIndex++];
                rtn[i] = SerializeUtilities.ConvertByteArrayToFloat(tmp, Endianness.little_endian);
            }
        }
        catch (IndexOutOfBoundsException e1) { }
        return rtn;
    }
}
