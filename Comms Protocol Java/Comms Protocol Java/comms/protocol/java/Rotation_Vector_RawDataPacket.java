package comms.protocol.java;

public class Rotation_Vector_RawDataPacket extends DataPacket
{
	private final int NUM_ELMTS_IN_QUAT = 4;
	private final int NUM_BYTES_IN_FLOAT = 4;
	
	public Rotation_Vector_RawDataPacket()
    {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, (short)16, new byte[16]);
    }
	
	public Rotation_Vector_RawDataPacket(byte[] payload) throws Exception
    {
		super(ValidPacketTypes.rotation_vector_raw_data_packet, (short)16, new byte[16]);
        Serialize(payload);
    }
	
	public Rotation_Vector_RawDataPacket(DataPacket packet) throws Exception
    {
        if ((packet.getPacketType() != ValidPacketTypes.rotation_vector_raw_data_packet) ||
            (packet.getExpectedLen() != (NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT)))
        {
            this.setPacketType(ValidPacketTypes.rotation_vector_raw_data_packet);
            this.setExpectedLen((short) (NUM_ELMTS_IN_QUAT * NUM_BYTES_IN_FLOAT));
            this.setPayload(new byte[this.getExpectedLen()]);
        }
        else
        {
        	this.setPacketType(packet.getPacketType());
        	this.setExpectedLen(packet.getExpectedLen());
            Serialize(packet.getPayload());
        }
    }
	
	public void Serialize(byte[] payload) throws Exception
    {
        if (payload.length < this.getExpectedLen())
            throw new Exception();

        this.setPayload(payload);
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
	
	public RotationVector_Quat GetQuat()
    {
        RotationVector_Quat rtn = new RotationVector_Quat();
        float[] vect = DeSerialize();
        if (vect.length == this.getExpectedLen() / 4)
        {
            rtn.x = vect[0];
            rtn.y = vect[1];
            rtn.z = vect[2];
            rtn.w = vect[3];
        }

        return rtn;
    }

}