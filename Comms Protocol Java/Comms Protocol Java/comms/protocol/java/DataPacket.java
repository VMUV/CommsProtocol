package comms.protocol.java;

public class DataPacket
{
	public static final short NumOverHeadBytes = 3;
    public static final int TypePos = 0;
    public static final int LenPos1 = 1;
    public static final int LenPos2 = 2;
    public static final int DataStartPos = 3;

	byte[] _payload;
    private ValidPacketTypes _type;
    private short _expectedLen;
    
    public DataPacket()
    {
    	this._type = ValidPacketTypes.end_valid_packet_types;
    	this._expectedLen = -1;
    }
    
    public DataPacket(ValidPacketTypes type, short len, byte[] payload)
    {
        this._type = type;
        this._expectedLen = len;
        this._payload = payload;
    }
    
    public int Serialize(byte[] stream, int streamOffset)
    {
        if ((streamOffset + NumOverHeadBytes) > stream.length)
            return streamOffset;

        int index = streamOffset + TypePos;
        _type = ValidPacketTypes.values()[stream[index++]];
        _expectedLen = (short)stream[index++];
        _expectedLen <<= 8;
        _expectedLen |= (short)stream[index++];

        if ((index + _expectedLen) <= stream.length)
        {
            _payload = new byte[_expectedLen];
            for (int i = 0; i < _expectedLen; i++)
                _payload[i] = stream[index++];
        }

        return index;
    }
    
    public byte[] getPayload()
    {
    	return _payload;
    }
    
    public void setPayload(byte[] payload)
    {
    	this._payload = payload;
    }
    
    public short getExpectedLen()
    {
    	return _expectedLen;
    }
    
    public void setExpectedLen(short expectedLen)
    {
    	this._expectedLen = expectedLen;
    }

	public ValidPacketTypes getPacketType() 
	{
		return _type;
	}

	public void setPacketType(ValidPacketTypes _type) 
	{
		this._type = _type;
	}
}
