package comms.protocol.java;

public class Motus_1_RawDataPacket extends DataPacket
{
	public Motus_1_RawDataPacket()
    {
		super(ValidPacketTypes.motus_1_raw_data_packet, (short)18, new byte[0]);
    }
	
	public void Serialize(byte[] payload) throws Exception
    {
        if (payload.length != this.getExpectedLen())
            throw new Exception();

        this.setPayload(payload);
    }
	
	public short[] DeSerialize()
    {
        short[] rtn = new short[this.getExpectedLen() / 2];
        int byteIndex = 0;
        byte[] bytePayload = this.getPayload();
        try
        {
            for (int i = 0; i < rtn.length; i++)
            {
                byte[] tmp = 
                    new byte[] { bytePayload[byteIndex++], bytePayload[byteIndex++] };
                rtn[i] = 
                    SerializeUtilities.ConvertByteArrayToShort(tmp, Endianness.little_endian);
            }
        }
        catch (IndexOutOfBoundsException e1) { }
        return rtn;
    }
}
