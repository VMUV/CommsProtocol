package comms.protocol.java;

public class Motus_1_RawDataPacket extends DataPacket
{
	public Motus_1_RawDataPacket()
    {
        this.setPacketType(ValidPacketTypes.motus_1_raw_data_packet);
        this.setExpectedLen((short)18);
        this.setPayload(new byte[0]);
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
	
	public String ToString()
    {
        String rtn = "";
        short[] vals = this.DeSerialize();
        if (vals.length == this.getExpectedLen() / 2)
        {
            rtn = Short.toString(vals[0]);
            for (int i = 1; i < vals.length; i++)
            {
                rtn += ",";
                rtn += Short.toString(vals[i]);
            }
        }
        return rtn;
    }
}
