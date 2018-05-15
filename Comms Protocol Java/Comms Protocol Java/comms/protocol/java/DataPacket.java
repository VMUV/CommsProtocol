package comms.protocol.java;

public class DataPacket {
	public static final short NumOverHeadBytes = 5;
	public static final int HeaderPos1 = 0;
	public static final int HeaderPos2 = 1;
	public static final int TypePos = 2;
	public static final int LenPos1 = 3;
	public static final int LenPos2 = 4;
	public static final int DataStartPos = 5;
	public static final byte Header1 = (byte) 0xFE;
	public static final byte Header2 = (byte) 0x5A;

	byte[] _payload;
	private ValidPacketTypes _type;
	private short _expectedLen;

	public DataPacket() {
		this._type = ValidPacketTypes.end_valid_packet_types;
		this._expectedLen = -1;
	}

	public DataPacket(ValidPacketTypes type, short len, byte[] payload) {
		this._type = type;
		this._expectedLen = len;
		this._payload = payload;
	}

	public int SerializeToStream(byte[] stream, int streamOffset) {
		if (_type.getValue() >= ValidPacketTypes.end_valid_packet_types.getValue())
			return streamOffset;

		if ((NumOverHeadBytes + _expectedLen) > stream.length)
			return streamOffset;

		int index = streamOffset;
		try {
			stream[index++] = (byte) (Header1 & 0xFF);
			stream[index++] = (byte) (Header2 & 0xFF);
			stream[index++] = (byte) (_type.getValue() & 0xFF);
			stream[index++] = (byte) ((_expectedLen >> 8) & 0xFF);
			stream[index++] = (byte) (_expectedLen & 0xFF);
			for (int i = 0; i < _expectedLen; i++)
				stream[index++] = (byte) (_payload[i] & 0xFF);
		} catch (IndexOutOfBoundsException e) {
			index--;
		}

		return index;
	}

	public int SerializeFromStream(byte[] stream, int streamOffset) {
		if ((streamOffset + NumOverHeadBytes) > stream.length)
			return streamOffset;

		int index = streamOffset + HeaderPos1;
		byte header1 = stream[index++];
		if (Header1 != header1)
			return index;
		byte header2 = stream[index++];
		if (Header2 != header2)
			return --index;
		try {
			_type = ValidPacketTypes.values()[stream[index++]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return index;
		}
		if (_type.getValue() >= ValidPacketTypes.end_valid_packet_types.getValue())
			return index;

		_expectedLen = (short) stream[index++];
		_expectedLen <<= 8;
		_expectedLen |= (short) stream[index++];

		if ((index + _expectedLen) <= stream.length) {
			_payload = new byte[_expectedLen];
			for (int i = 0; i < _expectedLen; i++)
				_payload[i] = stream[index++];
		} else {
			_type = ValidPacketTypes.end_valid_packet_types;
			_expectedLen = -1;
		}

		return index;
	}

	public byte[] getPayload() {
		return _payload;
	}

	public void setPayload(byte[] payload) {
		this._payload = payload;
	}

	public short getExpectedLen() {
		return _expectedLen;
	}

	public void setExpectedLen(short expectedLen) {
		this._expectedLen = expectedLen;
	}

	public ValidPacketTypes getPacketType() {
		return _type;
	}

	public void setPacketType(ValidPacketTypes _type) {
		if (_type.getValue() > ValidPacketTypes.end_valid_packet_types.getValue())
			this._type = ValidPacketTypes.end_valid_packet_types;
		else
			this._type = _type;
	}
}
