package comms.protocol.java;

import java.nio.ByteBuffer;

public class Motus_1_RawDataPacket extends DataPacket {
	public Motus_1_RawDataPacket() {
		super(ValidPacketTypes.motus_1_raw_data_packet, (short) 18, new byte[18]);
	}

	public Motus_1_RawDataPacket(byte[] payload) throws Exception {
		super(ValidPacketTypes.motus_1_raw_data_packet, (short) 18, new byte[18]);
		Serialize(payload);
	}

	public Motus_1_RawDataPacket(DataPacket packet) throws Exception {
		if ((packet.getPacketType() != ValidPacketTypes.motus_1_raw_data_packet) || (packet.getExpectedLen() != 18)) {
			this.setPacketType(ValidPacketTypes.motus_1_raw_data_packet);
			this.setExpectedLen((short) 18);
			this.setPayload(new byte[this.getExpectedLen()]);
		} else {
			this.setPacketType(packet.getPacketType());
			this.setExpectedLen(packet.getExpectedLen());
			Serialize(packet.getPayload());
		}
	}

	public void Serialize(byte[] payload) throws Exception {
		if (payload.length != this.getExpectedLen())
			throw new Exception();

		this.setPayload(payload);
	}

	public void Serialize(short[] payload) throws Exception {
		if (payload.length != (this.getExpectedLen() / 2))
			throw new Exception();
		
		ByteBuffer buff = ByteBuffer.wrap(this.getPayload());
		for (int i = 0; i < payload.length; i++)
			buff.putShort(payload[i]);
	}

	public short[] DeSerialize() {
		short[] rtn = new short[this.getExpectedLen() / 2];
		ByteBuffer buff = ByteBuffer.wrap(this.getPayload());
		for (int i = 0; i < rtn.length; i++)
			rtn[i] = buff.getShort();
		return rtn;
	}
}
