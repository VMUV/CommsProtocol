package comms.protocol.java;

public class Rotation_Vector_RawDataPacket extends DataPacket
{
	private final int NUM_ELMTS_IN_QUAT = 4;
	private final int NUM_BYTES_IN_FLOAT = 4;
	
	public Rotation_Vector_RawDataPacket()
    {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, (short)16, new byte[0]);
    }
	
	public void Serialize(float[] rawSensorData) throws Exception
    {
		if (rawSensorData.length < NUM_ELMTS_IN_QUAT)
			throw new Exception();
		byte[] payload = new byte[NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT];
		int indexToInsertElement = 0;
		for (int i = 0; i < NUM_ELMTS_IN_QUAT; i++)
		{
			indexToInsertElement = SerializeUtilities.BufferFloatInToByteArray(rawSensorData[i], payload, indexToInsertElement, Endianness.little_endian);
		}

        this.setPayload(payload);
    }
	
	public float[] DeSerialize()
    {
        float[] rtn = new float[this.getExpectedLen() / NUM_BYTES_IN_FLOAT];
        int byteIndex = 0;
        byte[] bytePayload = this.getPayload();
        try
        {
            for (int i = 0; i < rtn.length; i++)
            {
                byte[] tmp = new byte[NUM_BYTES_IN_FLOAT];
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
