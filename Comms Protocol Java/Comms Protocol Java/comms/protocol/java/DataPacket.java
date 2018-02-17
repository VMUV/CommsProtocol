package comms.protocol.java;

public class DataPacket 
{
	private byte[] _payload;
    private ValidPacketTypes _type;
    private short _expectedLen;
    
    public DataPacket()
    {
    	this._payload = new byte[0];
    	this._type = ValidPacketTypes.test_packet;
    	this._expectedLen = -1;
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
